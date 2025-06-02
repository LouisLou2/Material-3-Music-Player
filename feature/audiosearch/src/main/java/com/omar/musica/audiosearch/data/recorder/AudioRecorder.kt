package com.omar.musica.audiosearch.data.recorder

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 音频录制器核心类
 * 负责管理 Android 系统的音频录制功能
 * 
 * 技术说明：
 * - 使用 AudioRecord 进行低级别的音频录制
 * - 自动尝试不同的音频源以提高兼容性
 * - 管理音频焦点确保录制质量
 * - 配置为 44.1kHz 采样率，16位深度，单声道
 * - 录制的音频数据存储在内存中的字节数组
 */
@Singleton
class AudioRecorder @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    // 录制参数配置 - 这些是音频质量的标准参数
    private companion object {
        const val SAMPLE_RATE = 44100  // 44.1kHz - CD音质采样率
        const val CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO  // 单声道
        const val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT  // 16位深度
        const val RECORDING_TIMEOUT_MS = 30000L  // 30秒录制超时
        const val INITIALIZATION_DELAY_MS = 200L  // 增加初始化延迟
        const val READ_DELAY_MS = 10L  // 读取间隔
    }
    
    // 状态管理
    private val _recordingStatus = MutableStateFlow(RecordingStatus.IDLE)
    val recordingStatus: StateFlow<RecordingStatus> = _recordingStatus.asStateFlow()
    
    // 录制相关组件
    private var audioRecord: AudioRecord? = null
    private var recordingJob: Job? = null
    private var audioData: ByteArray? = null
    private var currentAudioSource: Int = MediaRecorder.AudioSource.MIC
    
    // 音频管理
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private var audioFocusRequest: AudioFocusRequest? = null
    private var originalAudioMode: Int = AudioManager.MODE_NORMAL
    
    // 可尝试的音频源列表（按优先级排序）
    private val audioSources = listOf(
        MediaRecorder.AudioSource.VOICE_RECOGNITION,  // 最适合语音识别
        MediaRecorder.AudioSource.MIC,                // 标准麦克风
        MediaRecorder.AudioSource.VOICE_COMMUNICATION, // 语音通话
        MediaRecorder.AudioSource.DEFAULT,            // 默认源
        MediaRecorder.AudioSource.CAMCORDER          // 摄像机音频
    )
    
    // 计算音频缓冲区大小 - 确保录制质量
    private val bufferSize: Int by lazy {
        val minBufferSize = AudioRecord.getMinBufferSize(
            SAMPLE_RATE,
            CHANNEL_CONFIG, 
            AUDIO_FORMAT
        )
        // 确保缓冲区大小有效
        if (minBufferSize == AudioRecord.ERROR_BAD_VALUE || minBufferSize == AudioRecord.ERROR) {
            8192 // 使用默认缓冲区大小
        } else {
            minBufferSize * 4 // 使用4倍最小缓冲区大小提高稳定性
        }
    }
    
    /**
     * 开始录音
     * 
     * 工作流程：
     * 1. 检查当前状态，避免重复录制
     * 2. 请求音频焦点
     * 3. 配置音频管理器
     * 4. 尝试不同的音频源创建 AudioRecord 对象
     * 5. 启动后台录制任务
     * 6. 更新状态为 RECORDING
     */
    fun startRecording() {
        if (_recordingStatus.value == RecordingStatus.RECORDING) {
            Timber.w("录音已在进行中，忽略重复请求")
            return
        }
        
        // 先清理之前的资源
        cleanupAudioRecord()
        
        try {
            // 1. 请求音频焦点
            if (!requestAudioFocus()) {
                Timber.e("无法获取音频焦点")
                _recordingStatus.value = RecordingStatus.ERROR
                return
            }
            
            // 2. 配置音频管理器
            configureAudioManager()
            
            // 3. 尝试不同的音频源
            val audioRecordResult = createAudioRecord()
            if (!audioRecordResult.isSuccess) {
                Timber.e("所有音频源都无法初始化: ${audioRecordResult.errorMessage}")
                releaseAudioFocus()
                _recordingStatus.value = RecordingStatus.ERROR
                return
            }
            
            Timber.d("使用音频源: ${getAudioSourceName(currentAudioSource)}")
            
            val record = audioRecord!!
            
            // 4. 开始录制
            record.startRecording()
            
            // 5. 等待录制稳定
            Thread.sleep(INITIALIZATION_DELAY_MS)
            
            // 6. 检查录制状态
            if (record.recordingState != AudioRecord.RECORDSTATE_RECORDING) {
                Timber.e("AudioRecord 开始录制失败，当前状态: ${record.recordingState}")
                cleanupAudioRecord()
                releaseAudioFocus()
                _recordingStatus.value = RecordingStatus.ERROR
                return
            }
            
            _recordingStatus.value = RecordingStatus.RECORDING
            
            // 7. 启动后台录制任务
            startRecordingInBackground()
            
            Timber.d("录音开始成功，使用音频源: ${getAudioSourceName(currentAudioSource)}")
            
        } catch (e: SecurityException) {
            Timber.e(e, "录音权限被拒绝")
            releaseAudioFocus()
            _recordingStatus.value = RecordingStatus.ERROR
        } catch (e: Exception) {
            Timber.e(e, "录音启动失败")
            releaseAudioFocus()
            _recordingStatus.value = RecordingStatus.ERROR
        }
    }
    
    /**
     * 请求音频焦点
     */
    private fun requestAudioFocus(): Boolean {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Android 8.0+ 使用 AudioFocusRequest
                val audioAttributes = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build()
                
                audioFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
                    .setAudioAttributes(audioAttributes)
                    .setAcceptsDelayedFocusGain(false)
                    .setOnAudioFocusChangeListener { focusChange ->
                        Timber.d("音频焦点变化: $focusChange")
                        if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                            // 失去焦点时停止录制
                            stopRecording()
                        }
                    }
                    .build()
                
                val result = audioManager.requestAudioFocus(audioFocusRequest!!)
                val success = result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
                Timber.d("请求音频焦点结果: $result, 成功: $success")
                success
            } else {
                // Android 8.0以下使用旧API
                @Suppress("DEPRECATION")
                val result = audioManager.requestAudioFocus(
                    { focusChange ->
                        Timber.d("音频焦点变化: $focusChange")
                        if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                            stopRecording()
                        }
                    },
                    AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT
                )
                val success = result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
                Timber.d("请求音频焦点结果: $result, 成功: $success")
                success
            }
        } catch (e: Exception) {
            Timber.e(e, "请求音频焦点失败")
            false
        }
    }
    
    /**
     * 配置音频管理器
     */
    private fun configureAudioManager() {
        try {
            // 保存原始音频模式
            originalAudioMode = audioManager.mode
            
            // 设置为通信模式以获得更好的录制质量
            audioManager.mode = AudioManager.MODE_IN_COMMUNICATION
            
            // 禁用扬声器
            audioManager.isSpeakerphoneOn = false
            
            // 如果有蓝牙，暂时禁用
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                // Android 12+ 的处理
                audioManager.isMicrophoneMute = false
            }
            
            Timber.d("音频管理器配置完成，模式: ${audioManager.mode}")
            
        } catch (e: Exception) {
            Timber.e(e, "配置音频管理器失败")
        }
    }
    
    /**
     * 释放音频焦点
     */
    private fun releaseAudioFocus() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                audioFocusRequest?.let { request ->
                    audioManager.abandonAudioFocusRequest(request)
                    audioFocusRequest = null
                }
            } else {
                @Suppress("DEPRECATION")
                audioManager.abandonAudioFocus(null)
            }
            
            // 恢复原始音频模式
            audioManager.mode = originalAudioMode
            
            Timber.d("音频焦点已释放，音频模式已恢复")
            
        } catch (e: Exception) {
            Timber.e(e, "释放音频焦点失败")
        }
    }
    
    /**
     * 尝试创建 AudioRecord 对象
     * 按优先级尝试不同的音频源
     */
    private fun createAudioRecord(): AudioRecordResult {
        Timber.d("开始尝试创建AudioRecord，缓冲区大小: $bufferSize")
        
        // 首先验证缓冲区大小
        if (bufferSize <= 0) {
            return AudioRecordResult(false, "无效的缓冲区大小: $bufferSize")
        }
        
        val failedSources = mutableListOf<String>()
        
        for (audioSource in audioSources) {
            try {
                Timber.d("尝试音频源: ${getAudioSourceName(audioSource)}")
                
                val record = AudioRecord(
                    audioSource,
                    SAMPLE_RATE,
                    CHANNEL_CONFIG,
                    AUDIO_FORMAT,
                    bufferSize
                )
                
                when (record.state) {
                    AudioRecord.STATE_INITIALIZED -> {
                        // 进一步测试录制能力
                        val testResult = testRecordingCapability(record, audioSource)
                        if (testResult.isSuccess) {
                            Timber.d("AudioRecord 初始化并测试成功，音频源: ${getAudioSourceName(audioSource)}")
                            audioRecord = record
                            currentAudioSource = audioSource
                            return AudioRecordResult(true, null)
                        } else {
                            Timber.w("AudioRecord 测试失败: ${testResult.errorMessage}")
                            failedSources.add("${getAudioSourceName(audioSource)}: ${testResult.errorMessage}")
                            record.release()
                            continue
                        }
                    }
                    AudioRecord.STATE_UNINITIALIZED -> {
                        val errorMsg = "初始化失败"
                        Timber.w("AudioRecord 初始化失败，音频源: ${getAudioSourceName(audioSource)}")
                        failedSources.add("${getAudioSourceName(audioSource)}: $errorMsg")
                        record.release()
                        continue
                    }
                    else -> {
                        val errorMsg = "未知状态: ${record.state}"
                        Timber.w("AudioRecord 状态异常: ${record.state}，音频源: ${getAudioSourceName(audioSource)}")
                        failedSources.add("${getAudioSourceName(audioSource)}: $errorMsg")
                        record.release()
                        continue
                    }
                }
                
            } catch (e: SecurityException) {
                val errorMsg = "权限不足: ${e.message}"
                Timber.w("音频源 ${getAudioSourceName(audioSource)} 权限不足: ${e.message}")
                failedSources.add("${getAudioSourceName(audioSource)}: $errorMsg")
                continue
            } catch (e: IllegalArgumentException) {
                val errorMsg = "参数无效: ${e.message}"
                Timber.w("音频源 ${getAudioSourceName(audioSource)} 参数无效: ${e.message}")
                failedSources.add("${getAudioSourceName(audioSource)}: $errorMsg")
                continue
            } catch (e: Exception) {
                val errorMsg = "创建失败: ${e.message}"
                Timber.w("音频源 ${getAudioSourceName(audioSource)} 创建失败: ${e.message}")
                failedSources.add("${getAudioSourceName(audioSource)}: $errorMsg")
                continue
            }
        }
        
        val errorMessage = "所有音频源都无法初始化:\n${failedSources.joinToString("\n")}"
        return AudioRecordResult(false, errorMessage)
    }
    
    /**
     * 测试录制能力
     */
    private fun testRecordingCapability(record: AudioRecord, audioSource: Int): AudioRecordResult {
        return try {
            // 尝试短暂录制测试
            record.startRecording()
            Thread.sleep(50) // 等待50ms
            
            val recordingState = record.recordingState
            if (recordingState == AudioRecord.RECORDSTATE_RECORDING) {
                // 尝试读取一些数据
                val testBuffer = ByteArray(1024)
                val readBytes = record.read(testBuffer, 0, testBuffer.size)
                
                record.stop()
                
                when {
                    readBytes > 0 -> {
                        AudioRecordResult(true, "测试成功，读取 $readBytes 字节")
                    }
                    readBytes == AudioRecord.ERROR_INVALID_OPERATION -> {
                        AudioRecordResult(false, "无法读取数据 - ERROR_INVALID_OPERATION")
                    }
                    readBytes == AudioRecord.ERROR_BAD_VALUE -> {
                        AudioRecordResult(false, "无法读取数据 - ERROR_BAD_VALUE")
                    }
                    readBytes == AudioRecord.ERROR -> {
                        AudioRecordResult(false, "无法读取数据 - ERROR")
                    }
                    else -> {
                        AudioRecordResult(false, "读取返回异常值: $readBytes")
                    }
                }
            } else {
                record.stop()
                AudioRecordResult(false, "无法开始录制，状态: $recordingState")
            }
        } catch (e: Exception) {
            try { record.stop() } catch (e2: Exception) { /* ignore */ }
            AudioRecordResult(false, "测试录制失败: ${e.message}")
        }
    }
    
    /**
     * 获取音频源名称
     */
    private fun getAudioSourceName(audioSource: Int): String {
        return when (audioSource) {
            MediaRecorder.AudioSource.MIC -> "MIC"
            MediaRecorder.AudioSource.VOICE_RECOGNITION -> "VOICE_RECOGNITION"
            MediaRecorder.AudioSource.VOICE_COMMUNICATION -> "VOICE_COMMUNICATION"
            MediaRecorder.AudioSource.DEFAULT -> "DEFAULT"
            MediaRecorder.AudioSource.CAMCORDER -> "CAMCORDER"
            else -> "UNKNOWN($audioSource)"
        }
    }
    
    /**
     * 停止录音
     * 
     * 工作流程：
     * 1. 更新状态让后台任务知道要停止
     * 2. 停止 AudioRecord 的录制
     * 3. 等待后台任务完成数据保存
     * 4. 释放音频焦点
     * 5. 清理资源
     */
    fun stopRecording() {
        if (_recordingStatus.value != RecordingStatus.RECORDING) {
            Timber.w("当前没有在录音，忽略停止请求")
            return
        }
        
        try {
            Timber.d("开始停止录音流程")
            
            // 1. 先更新状态，让后台任务知道要停止读取
            _recordingStatus.value = RecordingStatus.COMPLETED
            
            // 2. 停止AudioRecord录制
            audioRecord?.let { record ->
                if (record.recordingState == AudioRecord.RECORDSTATE_RECORDING) {
                    record.stop()
                    Timber.d("AudioRecord 已停止")
                }
            }
            
            // 3. 给后台任务时间完成数据保存
            Thread.sleep(500) // 等待500ms让后台任务保存数据
            
            // 4. 现在才取消后台任务
            recordingJob?.cancel()
            recordingJob = null
            
            // 5. 清理AudioRecord资源
            audioRecord?.let { record ->
                record.release()
                Timber.d("AudioRecord 已释放")
            }
            audioRecord = null
            
            // 6. 释放音频焦点
            releaseAudioFocus()
            
            Timber.d("录音停止成功，数据大小: ${audioData?.size ?: 0} bytes")
            
        } catch (e: Exception) {
            Timber.e(e, "录音停止失败")
            _recordingStatus.value = RecordingStatus.ERROR
        }
    }
    
    /**
     * 获取录制的音频数据
     * 
     * @return 录制的音频字节数组，如果没有录制数据则返回null
     */
    fun getRecordedAudioData(): ByteArray? {
        return audioData
    }
    
    /**
     * 重置录制器状态
     * 清除之前的录制数据，准备新的录制
     */
    fun reset() {
        stopRecording()
        audioData = null
        releaseAudioFocus()
        _recordingStatus.value = RecordingStatus.IDLE
        Timber.d("录制器已重置")
    }
    
    /**
     * 清理AudioRecord资源
     */
    private fun cleanupAudioRecord() {
        try {
            recordingJob?.cancel()
            recordingJob = null
            
            audioRecord?.let { record ->
                if (record.state == AudioRecord.STATE_INITIALIZED) {
                    if (record.recordingState == AudioRecord.RECORDSTATE_RECORDING) {
                        record.stop()
                    }
                    record.release()
                }
            }
            audioRecord = null
        } catch (e: Exception) {
            Timber.e(e, "清理AudioRecord资源失败")
        }
    }
    
    /**
     * 后台录制任务
     * 在协程中持续读取音频数据并存储到内存
     */
    private fun startRecordingInBackground() {
        recordingJob = CoroutineScope(Dispatchers.IO).launch {
            val outputStream = ByteArrayOutputStream()
            val buffer = ByteArray(bufferSize)
            var totalBytes = 0
            val startTime = System.currentTimeMillis()
            var consecutiveErrors = 0
            val maxConsecutiveErrors = 10
            
            try {
                Timber.d("开始录制后台任务，缓冲区大小: ${buffer.size}")
                
                // 持续读取音频数据直到停止
                while (_recordingStatus.value == RecordingStatus.RECORDING) {
                    val currentTime = System.currentTimeMillis()
                    
                    // 检查录制超时
                    if (currentTime - startTime > RECORDING_TIMEOUT_MS) {
                        Timber.w("录制超时，自动停止")
                        break
                    }
                    
                    val record = audioRecord
                    if (record == null) {
                        Timber.e("AudioRecord 对象为空")
                        break
                    }
                    
                    // 检查AudioRecord状态 - 如果已经停止就退出
                    if (record.state != AudioRecord.STATE_INITIALIZED) {
                        Timber.e("AudioRecord 状态异常: ${record.state}")
                        break
                    }
                    
                    // 如果AudioRecord已经停止，也要退出循环保存数据
                    if (record.recordingState != AudioRecord.RECORDSTATE_RECORDING) {
                        Timber.d("AudioRecord 已停止录制，退出循环保存数据")
                        break
                    }
                    
                    val readBytes = record.read(buffer, 0, buffer.size)
                    
                    when {
                        readBytes > 0 -> {
                            outputStream.write(buffer, 0, readBytes)
                            totalBytes += readBytes
                            consecutiveErrors = 0 // 重置错误计数
                        }
                        readBytes == AudioRecord.ERROR_INVALID_OPERATION -> {
                            Timber.e("AudioRecord 读取错误: ERROR_INVALID_OPERATION")
                            consecutiveErrors++
                        }
                        readBytes == AudioRecord.ERROR_BAD_VALUE -> {
                            Timber.e("AudioRecord 读取错误: ERROR_BAD_VALUE")
                            consecutiveErrors++
                        }
                        readBytes == AudioRecord.ERROR -> {
                            Timber.e("AudioRecord 读取错误: ERROR")
                            consecutiveErrors++
                        }
                        readBytes == 0 -> {
                            consecutiveErrors++
                        }
                        else -> {
                            Timber.w("AudioRecord 读取返回异常值: $readBytes")
                            consecutiveErrors++
                        }
                    }
                    
                    // 如果连续错误太多，停止录制
                    if (consecutiveErrors >= maxConsecutiveErrors) {
                        Timber.e("连续错误过多 ($consecutiveErrors)，停止录制")
                        break
                    }
                    
                    // 短暂休眠避免过度占用CPU
                    delay(READ_DELAY_MS)
                }
                
                // 录制完成，保存数据
                audioData = outputStream.toByteArray()
                val duration = System.currentTimeMillis() - startTime
                
                if (audioData?.isEmpty() == true) {
                    Timber.w("录制的音频数据为空")
                    _recordingStatus.value = RecordingStatus.ERROR
                } else if (audioData == null) {
                    Timber.e("录制数据为null")
                    _recordingStatus.value = RecordingStatus.ERROR
                } else {
                    Timber.d("录制数据收集完成，总大小: ${audioData?.size} bytes，持续时间: ${duration}ms")
                    
                    // 检查数据质量
                    val nonZeroCount = audioData!!.count { it != 0.toByte() }
                    if (nonZeroCount == 0) {
                        Timber.w("所有录制数据都为0，可能是音频设备问题")
                    }
                }
                
            } catch (e: Exception) {
                Timber.e(e, "录制过程中发生错误")
                _recordingStatus.value = RecordingStatus.ERROR
            } finally {
                try {
                    outputStream.close()
                } catch (e: Exception) {
                    Timber.e(e, "关闭输出流失败")
                }
            }
        }
    }
    
    /**
     * AudioRecord 创建结果
     */
    private data class AudioRecordResult(
        val isSuccess: Boolean,
        val errorMessage: String?
    )
} 