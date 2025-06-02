package com.omar.musica.audiosearch.ui

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.musica.audiosearch.config.AudioSearchConfig
import com.omar.musica.audiosearch.data.recorder.AudioRecorder
import com.omar.musica.audiosearch.data.recorder.RecordingStatus
import com.omar.musica.audiosearch.model.RecognizedSong
import com.omar.musica.audiosearch.util.AudioFileUtils
import com.omar.musica.network.data.AudioRecognitionSource
import com.omar.musica.network.model.NetworkErrorException
import com.omar.musica.network.model.NotFoundException
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import javax.inject.Inject

/**
 * 听歌识曲功能的ViewModel
 * 
 * 职责：
 * 1. 管理UI状态（录制状态、权限状态、错误信息等）
 * 2. 处理用户操作（开始录音、停止录音、开始识别）
 * 3. 权限管理（检查和请求录音权限）
 * 4. 与AudioRecorder和AudioRecognitionSource协调工作
 * 5. 计算录制时长并更新UI
 * 6. 保存录制的音频文件用于调试
 */
@HiltViewModel
class AudioSearchViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val audioRecorder: AudioRecorder,
    private val audioRecognitionSource: AudioRecognitionSource
) : ViewModel() {
    
    // UI状态管理
    private val _uiState = MutableStateFlow(AudioSearchUiState())
    val uiState: StateFlow<AudioSearchUiState> = _uiState.asStateFlow()
    
    // 录制时长计时器
    private var durationTimerJob: Job? = null
    
    // 识别任务
    private var recognitionJob: Job? = null
    
    init {
        // 初始化时检查权限
        checkRecordPermission()
        
        // 监听录制状态变化
        observeRecordingStatus()
        
        // 检查API密钥配置
        checkApiConfiguration()
        
        // 清理旧的录制文件
        AudioFileUtils.cleanupOldAudioFiles(context)
    }
    
    /**
     * 开始录音
     * 
     * 流程：
     * 1. 检查权限
     * 2. 重置之前的录制数据
     * 3. 开始录制
     * 4. 启动计时器
     */
    fun startRecording() {
        Timber.d("用户请求开始录音")
        
        // 检查权限
        if (!_uiState.value.hasRecordPermission) {
            updateErrorMessage("Microphone permission is required")
            return
        }
        
        // 检查当前状态
        if (!_uiState.value.canStartRecording) {
            Timber.w("当前状态不允许开始录音: ${_uiState.value.recordingStatus}")
            return
        }
        
        try {
            // 重置录制器和UI状态
            audioRecorder.reset()
            clearError()
            resetRecognitionState()
            
            // 开始录制
            audioRecorder.startRecording()
            
            // 启动录制时长计时器
            startDurationTimer()
            
            Timber.d("录音已开始")
            
        } catch (e: Exception) {
            Timber.e(e, "开始录音失败")
            updateErrorMessage("Failed to start recording: ${e.message}")
        }
    }
    
    /**
     * 停止录音
     * 
     * 流程：
     * 1. 停止AudioRecorder
     * 2. 停止计时器
     * 3. 获取录制的音频数据
     * 4. 保存音频文件用于调试
     * 5. 自动开始识别
     */
    fun stopRecording() {
        Timber.d("用户请求停止录音")
        
        if (!_uiState.value.canStopRecording) {
            Timber.w("当前状态不允许停止录音: ${_uiState.value.recordingStatus}")
            return
        }
        
        try {
            // 停止录制
            audioRecorder.stopRecording()
            
            // 停止计时器
            stopDurationTimer()
            
            // 检查录制时长
            val durationSeconds = _uiState.value.recordingDurationSeconds
            if (durationSeconds < AudioSearchConfig.MIN_RECORDING_DURATION_SECONDS) {
                updateErrorMessage("Recording too short, minimum ${AudioSearchConfig.MIN_RECORDING_DURATION_SECONDS} seconds required")
                return
            }
            
            // 获取录制数据
            val audioData = audioRecorder.getRecordedAudioData()
            Timber.d("录音已停止，数据大小: ${audioData?.size ?: 0} bytes")
            
            // 保存录制的音频文件用于调试
            if (audioData != null && audioData.isNotEmpty()) {
                val savedFile = AudioFileUtils.saveAudioToFile(
                    context = context,
                    audioData = audioData,
                    fileName = "recording_${System.currentTimeMillis()}_${durationSeconds}s"
                )
                if (savedFile != null) {
                    Timber.d("录制文件已保存: ${savedFile.absolutePath}")
                    // 自动开始识别
                    startAudioRecognition()
                } else {
                    Timber.w("保存录制文件失败")
                }
            } else {
                updateErrorMessage("Recording failed: no audio data captured")
            }
            
        } catch (e: Exception) {
            Timber.e(e, "停止录音失败")
            updateErrorMessage("Failed to stop recording: ${e.message}")
        }
    }
    
    /**
     * 开始音频识别
     */
    private fun startAudioRecognition() {
        Timber.d("开始音频识别")
        
        // 检查API配置
        if (!AudioSearchConfig.isApiKeyConfigured()) {
            updateErrorMessage("API key not configured. Please set your ACRCloud keys in AudioSearchConfig")
            return
        }
        
        // 取消之前的识别任务
        recognitionJob?.cancel()
        
        // 更新UI状态为识别中
        _uiState.value = _uiState.value.copy(
            recognitionStatus = RecognitionStatus.RECOGNIZING,
            recognitionProgressText = "Identifying song..."
        )
        
        recognitionJob = viewModelScope.launch {
            try {
                // 创建临时音频文件
                val audioFile = createTempAudioFile()
                if (audioFile == null) {
                    updateErrorMessage("Failed to create audio file")
                    updateRecognitionStatus(RecognitionStatus.FAILED)
                    return@launch
                }
                
                // 更新进度
                _uiState.value = _uiState.value.copy(
                    recognitionProgressText = "Connecting to recognition service..."
                )
                
                // 调用识别API
                val response = audioRecognitionSource.recognizeAudio(
                    audioFile = audioFile,
                    accessKey = AudioSearchConfig.ACCESS_KEY,
                    accessSecret = AudioSearchConfig.ACCESS_SECRET
                )
                
                // 解析识别结果
                val recognizedSong = parseRecognitionResponse(response)
                
                if (recognizedSong != null) {
                    // 识别成功
                    _uiState.value = _uiState.value.copy(
                        recognitionStatus = RecognitionStatus.SUCCESS,
                        recognizedSong = recognizedSong,
                        recognitionProgressText = null
                    )
                    Timber.d("识别成功: ${recognizedSong.title} - ${recognizedSong.getArtistsString()}")
                } else {
                    // 没有找到匹配的歌曲
                    updateRecognitionStatus(RecognitionStatus.FAILED)
                }
                
                // 清理临时文件
                audioFile.delete()
                
            } catch (e: NotFoundException) {
                Timber.w("识别失败：没有找到匹配的歌曲")
                updateRecognitionStatus(RecognitionStatus.FAILED)
            } catch (e: NetworkErrorException) {
                Timber.e(e, "识别失败：网络错误")
                updateRecognitionStatus(RecognitionStatus.FAILED)
                updateErrorMessage("Recognition failed: ${e.message}")
            } catch (e: Exception) {
                Timber.e(e, "识别失败：未知错误")
                updateRecognitionStatus(RecognitionStatus.FAILED)
                updateErrorMessage("Recognition failed: ${e.message ?: "Unknown error"}")
            }
        }
    }
    
    /**
     * 创建临时音频文件
     * 将原始PCM数据转换为带有正确WAV头的音频文件
     */
    private suspend fun createTempAudioFile(): File? {
        return try {
            val audioData = audioRecorder.getRecordedAudioData()
            if (audioData == null || audioData.isEmpty()) {
                Timber.w("音频数据为空，无法创建文件")
                return null
            }
            
            // 检查音频数据质量
            val nonZeroCount = audioData.count { it != 0.toByte() }
            if (nonZeroCount < audioData.size / 10) { // 如果90%以上的数据都是0
                Timber.w("音频数据质量差，非零数据比例: ${nonZeroCount.toDouble() / audioData.size}")
            }
            
            val tempFile = File.createTempFile("recorded_audio", ".wav", context.cacheDir)
            
            // 创建带有WAV头的音频文件
            writeWavFile(tempFile, audioData)
            
            // 验证文件
            if (!tempFile.exists() || tempFile.length() == 0L) {
                Timber.e("创建的音频文件无效")
                return null
            }
            
            Timber.d("音频文件创建成功: ${tempFile.absolutePath}, 大小: ${tempFile.length()} bytes")
            tempFile
        } catch (e: Exception) {
            Timber.e(e, "创建临时音频文件失败")
            null
        }
    }
    
    /**
     * 将PCM数据写入WAV文件
     * 添加正确的WAV文件头以确保ACRCloud能正确识别
     */
    private fun writeWavFile(file: File, pcmData: ByteArray) {
        val sampleRate = 44100
        val channels = 1 // 单声道
        val bitsPerSample = 16
        val byteRate = sampleRate * channels * bitsPerSample / 8
        val blockAlign = channels * bitsPerSample / 8
        val dataSize = pcmData.size
        val fileSize = 36 + dataSize
        
        file.outputStream().use { out ->
            // WAV文件头（44字节）
            // RIFF头
            out.write("RIFF".toByteArray()) // ChunkID
            out.write(intToByteArray(fileSize)) // ChunkSize
            out.write("WAVE".toByteArray()) // Format
            
            // fmt子块
            out.write("fmt ".toByteArray()) // Subchunk1ID
            out.write(intToByteArray(16)) // Subchunk1Size (PCM = 16)
            out.write(shortToByteArray(1)) // AudioFormat (PCM = 1)
            out.write(shortToByteArray(channels)) // NumChannels
            out.write(intToByteArray(sampleRate)) // SampleRate
            out.write(intToByteArray(byteRate)) // ByteRate
            out.write(shortToByteArray(blockAlign)) // BlockAlign
            out.write(shortToByteArray(bitsPerSample)) // BitsPerSample
            
            // data子块
            out.write("data".toByteArray()) // Subchunk2ID
            out.write(intToByteArray(dataSize)) // Subchunk2Size
            out.write(pcmData) // 实际音频数据
        }
    }
    
    /**
     * 将整数转换为小端字节数组
     */
    private fun intToByteArray(value: Int): ByteArray {
        return byteArrayOf(
            (value and 0xFF).toByte(),
            ((value shr 8) and 0xFF).toByte(),
            ((value shr 16) and 0xFF).toByte(),
            ((value shr 24) and 0xFF).toByte()
        )
    }
    
    /**
     * 将短整数转换为小端字节数组
     */
    private fun shortToByteArray(value: Int): ByteArray {
        return byteArrayOf(
            (value and 0xFF).toByte(),
            ((value shr 8) and 0xFF).toByte()
        )
    }
    
    /**
     * 解析识别响应
     */
    private fun parseRecognitionResponse(response: com.omar.musica.network.model.AudioRecognitionResponse): RecognizedSong? {
        val musicData = response.metadata?.music?.firstOrNull() ?: return null
        
        return RecognizedSong(
            title = musicData.title,
            artists = musicData.artists.map { it.name },
            album = musicData.album?.name,
            durationMs = musicData.durationMs,
            genres = musicData.genres?.map { it.name },
            releaseDate = musicData.releaseDate,
            externalIds = musicData.externalIds?.let { extIds ->
                RecognizedSong.ExternalIds(
                    spotify = extIds.spotify,
                    youtube = extIds.youtube,
                    isrc = extIds.isrc
                )
            }
        )
    }
    
    /**
     * 重置录制状态
     * 清除所有数据，回到初始状态
     */
    fun resetRecording() {
        Timber.d("重置录制状态")
        
        stopDurationTimer()
        recognitionJob?.cancel()
        audioRecorder.reset()
        clearError()
        
        _uiState.value = _uiState.value.copy(
            recordingDurationSeconds = 0,
            recognitionStatus = RecognitionStatus.IDLE,
            recognizedSong = null,
            recognitionProgressText = null
        )
    }
    
    /**
     * 重新识别
     */
    fun retryRecognition() {
        if (_uiState.value.recordingStatus == RecordingStatus.COMPLETED) {
            startAudioRecognition()
        } else {
            updateErrorMessage("Please record audio first")
        }
    }
    
    /**
     * 处理权限请求结果
     * 
     * @param granted 用户是否授予了录音权限
     */
    fun onPermissionResult(granted: Boolean) {
        Timber.d("权限请求结果: $granted")
        
        _uiState.value = _uiState.value.copy(
            hasRecordPermission = granted,
            isCheckingPermission = false,
            errorMessage = if (!granted) "Microphone permission is required for audio search" else null
        )
    }
    
    /**
     * 清除错误信息
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
    
    /**
     * 检查录音权限
     */
    private fun checkRecordPermission() {
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
        
        _uiState.value = _uiState.value.copy(
            hasRecordPermission = hasPermission
        )
        
        Timber.d("录音权限检查结果: $hasPermission")
    }
    
    /**
     * 检查API配置
     */
    private fun checkApiConfiguration() {
        if (!AudioSearchConfig.isApiKeyConfigured()) {
            Timber.w("ACRCloud API密钥未配置")
            updateErrorMessage("Please configure your ACRCloud API keys in AudioSearchConfig")
        } else {
            Timber.d("API密钥已配置: ${AudioSearchConfig.getMaskedApiKey()}")
        }
    }
    
    /**
     * 监听AudioRecorder的状态变化
     */
    private fun observeRecordingStatus() {
        viewModelScope.launch {
            audioRecorder.recordingStatus.collect { status ->
                Timber.d("录制状态变化: $status")
                
                _uiState.value = _uiState.value.copy(
                    recordingStatus = status
                )
                
                // 如果录制失败，停止计时器并显示错误
                if (status == RecordingStatus.ERROR) {
                    stopDurationTimer()
                    updateErrorMessage("Recording error occurred")
                }
            }
        }
    }
    
    /**
     * 启动录制时长计时器
     * 每秒更新一次录制时长，用于UI显示
     */
    private fun startDurationTimer() {
        stopDurationTimer() // 确保没有重复的计时器
        
        durationTimerJob = viewModelScope.launch {
            var seconds = 0
            while (_uiState.value.recordingStatus == RecordingStatus.RECORDING) {
                _uiState.value = _uiState.value.copy(
                    recordingDurationSeconds = seconds
                )
                delay(1000) // 等待1秒
                seconds++
                
                // 检查最大录制时长
                if (seconds >= AudioSearchConfig.MAX_RECORDING_DURATION_SECONDS) {
                    Timber.d("达到最大录制时长，自动停止录音")
                    stopRecording()
                    break
                }
            }
        }
    }
    
    /**
     * 停止录制时长计时器
     */
    private fun stopDurationTimer() {
        durationTimerJob?.cancel()
        durationTimerJob = null
    }
    
    /**
     * 重置识别状态
     */
    private fun resetRecognitionState() {
        _uiState.value = _uiState.value.copy(
            recognitionStatus = RecognitionStatus.IDLE,
            recognizedSong = null,
            recognitionProgressText = null
        )
    }
    
    /**
     * 更新识别状态
     */
    private fun updateRecognitionStatus(status: RecognitionStatus) {
        _uiState.value = _uiState.value.copy(
            recognitionStatus = status,
            recognitionProgressText = null
        )
    }
    
    /**
     * 更新错误信息
     */
    private fun updateErrorMessage(message: String) {
        _uiState.value = _uiState.value.copy(errorMessage = message)
        Timber.e("UI错误: $message")
    }
    
    override fun onCleared() {
        super.onCleared()
        // ViewModel销毁时清理资源
        stopDurationTimer()
        recognitionJob?.cancel()
        audioRecorder.reset()
        Timber.d("AudioSearchViewModel已清理")
    }
} 