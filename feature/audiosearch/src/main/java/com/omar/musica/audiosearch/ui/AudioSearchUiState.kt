package com.omar.musica.audiosearch.ui

import com.omar.musica.audiosearch.data.recorder.RecordingStatus
import com.omar.musica.audiosearch.model.RecognizedSong

/**
 * 音频识别状态枚举
 */
enum class RecognitionStatus {
    /** 空闲状态 */
    IDLE,
    /** 正在识别 */
    RECOGNIZING,
    /** 识别成功 */
    SUCCESS,
    /** 识别失败 */
    FAILED
}

/**
 * 听歌识曲页面的UI状态
 * 
 * 包含录制状态、权限状态、识别结果等所有UI需要的信息
 */
data class AudioSearchUiState(
    // 录制相关状态
    val recordingStatus: RecordingStatus = RecordingStatus.IDLE,
    val recordingDurationSeconds: Int = 0,
    
    // 权限相关状态  
    val hasRecordPermission: Boolean = false,
    val isCheckingPermission: Boolean = false,
    
    // 识别相关状态
    val recognitionStatus: RecognitionStatus = RecognitionStatus.IDLE,
    val recognizedSong: RecognizedSong? = null,
    val recognitionProgressText: String? = null,
    
    // 错误和消息
    val errorMessage: String? = null
) {
    
    /**
     * 便利属性：判断是否可以开始录制
     * 需要同时满足：有权限 且 当前状态为空闲 且 不在识别中
     */
    val canStartRecording: Boolean
        get() = hasRecordPermission && 
                recordingStatus == RecordingStatus.IDLE && 
                recognitionStatus == RecognitionStatus.IDLE
    
    /**
     * 便利属性：判断是否可以停止录制  
     * 当前状态必须是正在录制
     */
    val canStopRecording: Boolean
        get() = recordingStatus == RecordingStatus.RECORDING
    
    /**
     * 便利属性：判断是否可以开始识别
     * 需要录制完成且不在识别中
     */
    val canStartRecognition: Boolean
        get() = recordingStatus == RecordingStatus.COMPLETED && 
                recognitionStatus == RecognitionStatus.IDLE
    
    /**
     * 便利属性：判断是否正在处理（录制或识别）
     */
    val isProcessing: Boolean
        get() = recordingStatus == RecordingStatus.RECORDING || 
                recognitionStatus == RecognitionStatus.RECOGNIZING
    
    /**
     * 便利属性：判断是否有识别结果
     */
    val hasRecognitionResult: Boolean
        get() = recognitionStatus == RecognitionStatus.SUCCESS && recognizedSong != null
} 