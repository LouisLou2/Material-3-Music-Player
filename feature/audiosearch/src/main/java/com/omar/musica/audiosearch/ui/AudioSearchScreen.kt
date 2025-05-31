package com.omar.musica.audiosearch.ui

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.omar.musica.audiosearch.data.recorder.RecordingStatus

/**
 * 听歌识曲主界面
 * 
 * 界面功能：
 * 1. 显示录音按钮（麦克风图标）
 * 2. 显示当前录制状态和时长
 * 3. 显示错误信息（如权限问题）
 * 4. 处理权限请求
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioSearchScreen(
    modifier: Modifier = Modifier,
    viewModel: AudioSearchViewModel = hiltViewModel()
) {
    // 收集UI状态
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    // 权限请求启动器
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        viewModel.onPermissionResult(granted)
    }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        
        // 标题
        Text(
            text = "听歌识曲",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 功能说明
        Text(
            text = "轻触麦克风按钮开始录音\n识别您听到的音乐",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        // 录制状态指示器
        RecordingStatusIndicator(
            recordingStatus = uiState.recordingStatus,
            durationSeconds = uiState.recordingDurationSeconds
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        // 主录音按钮
        RecordButton(
            canStartRecording = uiState.canStartRecording,
            canStopRecording = uiState.canStopRecording,
            hasPermission = uiState.hasRecordPermission,
            onStartRecording = { viewModel.startRecording() },
            onStopRecording = { viewModel.stopRecording() },
            onRequestPermission = { 
                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // 重置按钮（录制完成后显示）
        if (uiState.recordingStatus == RecordingStatus.COMPLETED) {
            OutlinedButton(
                onClick = { viewModel.resetRecording() },
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
                Text("重新录制")
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // 错误信息显示 - 使用let操作符避免智能转换问题
        uiState.errorMessage?.let { errorMessage ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = errorMessage,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * 录制状态指示器
 * 显示当前的录制状态和时长
 */
@Composable
private fun RecordingStatusIndicator(
    recordingStatus: RecordingStatus,
    durationSeconds: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 状态文字
        val statusText = when (recordingStatus) {
            RecordingStatus.IDLE -> "准备录音"
            RecordingStatus.RECORDING -> "录音中..."
            RecordingStatus.COMPLETED -> "录音完成"
            RecordingStatus.ERROR -> "录音失败"
        }
        
        val statusColor = when (recordingStatus) {
            RecordingStatus.IDLE -> MaterialTheme.colorScheme.onSurface
            RecordingStatus.RECORDING -> MaterialTheme.colorScheme.primary
            RecordingStatus.COMPLETED -> MaterialTheme.colorScheme.primary
            RecordingStatus.ERROR -> MaterialTheme.colorScheme.error
        }
        
        Text(
            text = statusText,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = statusColor
        )
        
        // 录制时长（只在录音时显示）
        if (recordingStatus == RecordingStatus.RECORDING) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = formatDuration(durationSeconds),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

/**
 * 主录音按钮
 * 根据当前状态显示不同的按钮（录音/停止/请求权限）
 */
@Composable
private fun RecordButton(
    canStartRecording: Boolean,
    canStopRecording: Boolean,
    hasPermission: Boolean,
    onStartRecording: () -> Unit,
    onStopRecording: () -> Unit,
    onRequestPermission: () -> Unit
) {
    val buttonSize = 120.dp
    
    when {
        // 没有权限时显示请求权限按钮
        !hasPermission -> {
            FilledTonalButton(
                onClick = onRequestPermission,
                modifier = Modifier.size(buttonSize),
                shape = CircleShape
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "请求录音权限",
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "允许录音",
                        fontSize = 12.sp
                    )
                }
            }
        }
        
        // 可以停止录音时显示停止按钮
        canStopRecording -> {
            FilledTonalButton(
                onClick = onStopRecording,
                modifier = Modifier.size(buttonSize),
                shape = CircleShape,
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Stop,
                    contentDescription = "停止录音",
                    modifier = Modifier.size(48.dp),
                    tint = Color.White
                )
            }
        }
        
        // 可以开始录音时显示录音按钮
        canStartRecording -> {
            FilledTonalButton(
                onClick = onStartRecording,
                modifier = Modifier.size(buttonSize),
                shape = CircleShape,
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "开始录音",
                    modifier = Modifier.size(48.dp),
                    tint = Color.White
                )
            }
        }
        
        // 其他状态显示禁用的按钮
        else -> {
            FilledTonalButton(
                onClick = { },
                enabled = false,
                modifier = Modifier.size(buttonSize),
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "录音按钮",
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    }
}

/**
 * 格式化录制时长为 MM:SS 格式
 */
private fun formatDuration(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return "%02d:%02d".format(minutes, remainingSeconds)
}