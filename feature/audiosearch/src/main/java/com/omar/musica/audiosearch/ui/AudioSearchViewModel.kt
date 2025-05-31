package com.omar.musica.audiosearch.ui

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.musica.audiosearch.data.recorder.AudioRecorder
import com.omar.musica.audiosearch.data.recorder.RecordingStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * 听歌识曲功能的ViewModel
 * 
 * 职责：
 * 1. 管理UI状态（录制状态、权限状态、错误信息等）
 * 2. 处理用户操作（开始录音、停止录音）
 * 3. 权限管理（检查和请求录音权限）
 * 4. 与AudioRecorder协调工作
 * 5. 计算录制时长并更新UI
 */
@HiltViewModel
class AudioSearchViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val audioRecorder: AudioRecorder
) : ViewModel() {
    
    // UI状态管理
    private val _uiState = MutableStateFlow(AudioSearchUiState())
    val uiState: StateFlow<AudioSearchUiState> = _uiState.asStateFlow()
    
    // 录制时长计时器
    private var durationTimerJob: Job? = null
    
    init {
        // 初始化时检查权限
        checkRecordPermission()
        
        // 监听录制状态变化
        observeRecordingStatus()
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
            updateErrorMessage("需要录音权限才能使用此功能")
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
            
            // 开始录制
            audioRecorder.startRecording()
            
            // 启动录制时长计时器
            startDurationTimer()
            
            Timber.d("录音已开始")
            
        } catch (e: Exception) {
            Timber.e(e, "开始录音失败")
            updateErrorMessage("开始录音失败: ${e.message}")
        }
    }
    
    /**
     * 停止录音
     * 
     * 流程：
     * 1. 停止AudioRecorder
     * 2. 停止计时器
     * 3. 获取录制的音频数据
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
            
            // 获取录制数据（为后续API调用做准备）
            val audioData = audioRecorder.getRecordedAudioData()
            Timber.d("录音已停止，数据大小: ${audioData?.size ?: 0} bytes")
            
            // TODO: 在Phase 2中，这里将调用识别API
            
        } catch (e: Exception) {
            Timber.e(e, "停止录音失败")
            updateErrorMessage("停止录音失败: ${e.message}")
        }
    }
    
    /**
     * 重置录制状态
     * 清除所有数据，回到初始状态
     */
    fun resetRecording() {
        Timber.d("重置录制状态")
        
        stopDurationTimer()
        audioRecorder.reset()
        clearError()
        
        _uiState.value = _uiState.value.copy(
            recordingDurationSeconds = 0
        )
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
            errorMessage = if (!granted) "需要录音权限才能使用听歌识曲功能" else null
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
                    updateErrorMessage("录制过程中发生错误")
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
        audioRecorder.reset()
        Timber.d("AudioSearchViewModel已清理")
    }
} 