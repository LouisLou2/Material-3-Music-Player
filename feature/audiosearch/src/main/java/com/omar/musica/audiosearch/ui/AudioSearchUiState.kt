package com.omar.musica.audiosearch.ui

import com.omar.musica.audiosearch.data.recorder.RecordingStatus

/**
 * 听歌识曲界面的UI状态
 * 
 * 这个类定义了界面需要显示的所有信息：
 * - 录制状态（是否在录音、是否完成等）
 * - 错误信息（权限问题、录制失败等）
 * - 是否有录制权限
 */
data class AudioSearchUiState(
    /** 当前录制状态 */
    val recordingStatus: RecordingStatus = RecordingStatus.IDLE,
    
    /** 错误消息，如果有错误则显示给用户 */
    val errorMessage: String? = null,
    
    /** 是否有录制音频的权限 */
    val hasRecordPermission: Boolean = false,
    
    /** 录制时长（秒） - 用于显示录制进度 */
    val recordingDurationSeconds: Int = 0,
    
    /** 是否正在检查权限 */
    val isCheckingPermission: Boolean = false
) {
    
    /**
     * 便利属性：判断是否可以开始录制
     * 需要同时满足：有权限 且 当前状态为空闲
     */
    val canStartRecording: Boolean
        get() = hasRecordPermission && recordingStatus == RecordingStatus.IDLE
    
    /**
     * 便利属性：判断是否可以停止录制  
     * 当前状态必须是正在录制
     */
    val canStopRecording: Boolean
        get() = recordingStatus == RecordingStatus.RECORDING
} 