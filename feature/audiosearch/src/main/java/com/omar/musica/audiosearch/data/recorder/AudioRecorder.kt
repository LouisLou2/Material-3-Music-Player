package com.omar.musica.audiosearch.data.recorder

import android.content.Context
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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
 * - 配置为 44.1kHz 采样率，16位深度，单声道
 * - 录制的音频数据存储在内存中的字节数组
 */
@Singleton
class AudioRecorder @Inject constructor() {
    
    // 录制参数配置 - 这些是音频质量的标准参数
    private companion object {
        const val SAMPLE_RATE = 44100  // 44.1kHz - CD音质采样率
        const val CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO  // 单声道
        const val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT  // 16位深度
    }
    
    // 状态管理
    private val _recordingStatus = MutableStateFlow(RecordingStatus.IDLE)
    val recordingStatus: StateFlow<RecordingStatus> = _recordingStatus.asStateFlow()
    
    // 录制相关组件
    private var audioRecord: AudioRecord? = null
    private var recordingJob: Job? = null
    private var audioData: ByteArray? = null
    
    // 计算音频缓冲区大小 - 确保录制质量
    private val bufferSize = AudioRecord.getMinBufferSize(
        SAMPLE_RATE,
        CHANNEL_CONFIG, 
        AUDIO_FORMAT
    )
    
    /**
     * 开始录音
     * 
     * 工作流程：
     * 1. 检查当前状态，避免重复录制
     * 2. 创建 AudioRecord 对象
     * 3. 启动后台录制任务
     * 4. 更新状态为 RECORDING
     */
    fun startRecording() {
        if (_recordingStatus.value == RecordingStatus.RECORDING) {
            Timber.w("录音已在进行中，忽略重复请求")
            return
        }
        
        try {
            // 创建 AudioRecord 实例
            audioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC,  // 使用麦克风作为音频源
                SAMPLE_RATE,
                CHANNEL_CONFIG,
                AUDIO_FORMAT,
                bufferSize
            )
            
            // 检查 AudioRecord 是否成功初始化
            if (audioRecord?.state != AudioRecord.STATE_INITIALIZED) {
                Timber.e("AudioRecord 初始化失败")
                _recordingStatus.value = RecordingStatus.ERROR
                return
            }
            
            // 开始录制
            audioRecord?.startRecording()
            _recordingStatus.value = RecordingStatus.RECORDING
            
            // 启动后台录制任务
            startRecordingInBackground()
            
            Timber.d("录音开始成功")
            
        } catch (e: SecurityException) {
            Timber.e(e, "录音权限被拒绝")
            _recordingStatus.value = RecordingStatus.ERROR
        } catch (e: Exception) {
            Timber.e(e, "录音启动失败")
            _recordingStatus.value = RecordingStatus.ERROR
        }
    }
    
    /**
     * 停止录音
     * 
     * 工作流程：
     * 1. 停止 AudioRecord 的录制
     * 2. 取消后台录制任务
     * 3. 更新状态为 COMPLETED
     * 4. 清理资源
     */
    fun stopRecording() {
        if (_recordingStatus.value != RecordingStatus.RECORDING) {
            Timber.w("当前没有在录音，忽略停止请求")
            return
        }
        
        try {
            // 停止录制
            audioRecord?.stop()
            audioRecord?.release()
            audioRecord = null
            
            // 取消录制任务
            recordingJob?.cancel()
            recordingJob = null
            
            _recordingStatus.value = RecordingStatus.COMPLETED
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
        _recordingStatus.value = RecordingStatus.IDLE
        Timber.d("录制器已重置")
    }
    
    /**
     * 后台录制任务
     * 在协程中持续读取音频数据并存储到内存
     */
    private fun startRecordingInBackground() {
        recordingJob = CoroutineScope(Dispatchers.IO).launch {
            val outputStream = ByteArrayOutputStream()
            val buffer = ByteArray(bufferSize)
            
            try {
                // 持续读取音频数据直到停止
                while (_recordingStatus.value == RecordingStatus.RECORDING) {
                    val readBytes = audioRecord?.read(buffer, 0, buffer.size) ?: 0
                    
                    if (readBytes > 0) {
                        outputStream.write(buffer, 0, readBytes)
                    }
                }
                
                // 录制完成，保存数据
                audioData = outputStream.toByteArray()
                Timber.d("录制数据收集完成，大小: ${audioData?.size} bytes")
                
            } catch (e: Exception) {
                Timber.e(e, "录制过程中发生错误")
                _recordingStatus.value = RecordingStatus.ERROR
            } finally {
                outputStream.close()
            }
        }
    }
} 