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
 * 听歌识曲界面的UI状态
 * 
 * 这个类定义了界面需要显示的所有信息：
 * - 录制状态（是否在录音、是否完成等）
 * - 识别状态（是否在识别、识别结果等）
 * - 错误信息（权限问题、录制失败等）
 * - 是否有录制权限
 */
data class AudioSearchUiState(
    /** 当前录制状态 */
    val recordingStatus: RecordingStatus = RecordingStatus.IDLE,
    
    /** 当前识别状态 */
    val recognitionStatus: RecognitionStatus = RecognitionStatus.IDLE,
    
    /** 错误消息，如果有错误则显示给用户 */
    val errorMessage: String? = null,
    
    /** 是否有录制音频的权限 */
    val hasRecordPermission: Boolean = false,
    
    /** 录制时长（秒） - 用于显示录制进度 */
    val recordingDurationSeconds: Int = 0,
    
    /** 是否正在检查权限 */
    val isCheckingPermission: Boolean = false,
    
    /** 识别到的歌曲信息 */
    val recognizedSong: RecognizedSong? = null,
    
    /** 识别进度文本 */
    val recognitionProgressText: String? = null
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