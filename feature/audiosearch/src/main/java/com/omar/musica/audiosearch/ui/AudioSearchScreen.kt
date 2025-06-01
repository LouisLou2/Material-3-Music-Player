package com.omar.musica.audiosearch.ui

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.omar.musica.audiosearch.data.recorder.RecordingStatus
import androidx.compose.animation.core.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer

/**
 * 听歌识曲主界面
 * 
 * 界面功能：
 * 1. 显示录音按钮（麦克风图标）
 * 2. 显示当前录制状态和时长
 * 3. 显示错误信息（如权限问题）
 * 4. 处理权限请求
 * 5. 音频诊断功能
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioSearchScreen(
    modifier: Modifier = Modifier,
    viewModel: AudioSearchViewModel = hiltViewModel(),
    onNavigateToSearch: (String) -> Unit = {}
) {
    // 收集UI状态
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    
    // 权限请求启动器
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        viewModel.onPermissionResult(granted)
    }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
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
        
        // 识别状态和结果显示
        RecognitionStatusSection(
            recognitionStatus = uiState.recognitionStatus,
            recognizedSong = uiState.recognizedSong,
            recognitionProgressText = uiState.recognitionProgressText,
            onRetryRecognition = { viewModel.retryRecognition() },
            onSearchLocally = onNavigateToSearch
        )
        
        // 重置按钮（录制完成后显示）
        if (uiState.recordingStatus == RecordingStatus.COMPLETED) {
            OutlinedButton(
                onClick = { viewModel.resetRecording() },
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
                Text("重新录制")
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
        
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
            
            Spacer(modifier = Modifier.height(16.dp))
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
 * 添加了动态效果：点击缩放动画和录音时的脉冲效果
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
    
    // 交互状态
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    // 点击缩放动画
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "button_scale"
    )
    
    // 录音时的脉冲动画
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_animation")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )
    
    // 录音时的外圈动画
    val outerRingAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "outer_ring_alpha"
    )
    
    Box(
        modifier = Modifier.size(buttonSize * 1.3f), // 给外圈留空间
        contentAlignment = Alignment.Center
    ) {
        // 录音时的外圈效果
        if (canStopRecording) {
            Box(
                modifier = Modifier
                    .size(buttonSize * 1.2f)
                    .scale(pulseScale)
                    .graphicsLayer {
                        alpha = outerRingAlpha
                    }
            ) {
                Card(
                    modifier = Modifier.fillMaxSize(),
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.3f)
                    )
                ) {}
            }
        }
        
        when {
            // 没有权限时显示请求权限按钮
            !hasPermission -> {
                FilledTonalButton(
                    onClick = onRequestPermission,
                    modifier = Modifier
                        .size(buttonSize)
                        .scale(scale),
                    shape = CircleShape,
                    interactionSource = interactionSource
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
                    modifier = Modifier
                        .size(buttonSize)
                        .scale(scale * pulseScale),
                    shape = CircleShape,
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    interactionSource = interactionSource
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
                    modifier = Modifier
                        .size(buttonSize)
                        .scale(scale),
                    shape = CircleShape,
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    interactionSource = interactionSource
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
                    modifier = Modifier
                        .size(buttonSize)
                        .scale(scale),
                    shape = CircleShape,
                    interactionSource = interactionSource
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
}

/**
 * 格式化录制时长为 MM:SS 格式
 */
private fun formatDuration(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return "%02d:%02d".format(minutes, remainingSeconds)
}

/**
 * 识别状态和结果展示区域
 */
@Composable
private fun RecognitionStatusSection(
    recognitionStatus: RecognitionStatus,
    recognizedSong: com.omar.musica.audiosearch.model.RecognizedSong?,
    recognitionProgressText: String?,
    onRetryRecognition: () -> Unit,
    onSearchLocally: (String) -> Unit
) {
    when (recognitionStatus) {
        RecognitionStatus.IDLE -> {
            // 空闲状态不显示任何内容
        }
        
        RecognitionStatus.RECOGNIZING -> {
            // 识别中状态
            RecognitionInProgressCard(progressText = recognitionProgressText)
        }
        
        RecognitionStatus.SUCCESS -> {
            // 识别成功状态
            recognizedSong?.let { song ->
                RecognitionSuccessCard(
                    song = song,
                    onRetryRecognition = onRetryRecognition,
                    onSearchLocally = { onSearchLocally(song.title) }
                )
            }
        }
        
        RecognitionStatus.FAILED -> {
            // 识别失败状态
            RecognitionFailedCard(onRetryRecognition = onRetryRecognition)
        }
    }
}

/**
 * 识别进行中的卡片
 */
@Composable
private fun RecognitionInProgressCard(progressText: String?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = progressText ?: "正在识别音乐...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * 识别成功的结果卡片
 */
@Composable
private fun RecognitionSuccessCard(
    song: com.omar.musica.audiosearch.model.RecognizedSong,
    onRetryRecognition: () -> Unit,
    onSearchLocally: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // 成功图标和标题
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "识别成功",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Text(
                    text = "识别成功！",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 歌曲信息
            SongInfoSection(song = song)
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // 操作按钮
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 重新识别按钮
                val retryInteractionSource = remember { MutableInteractionSource() }
                val retryIsPressed by retryInteractionSource.collectIsPressedAsState()
                val retryScale by animateFloatAsState(
                    targetValue = if (retryIsPressed) 0.95f else 1f,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                    label = "retry_button_scale"
                )
                
                OutlinedButton(
                    onClick = onRetryRecognition,
                    modifier = Modifier
                        .weight(1f)
                        .scale(retryScale),
                    interactionSource = retryInteractionSource
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("重试")
                }
                
                // 本地搜索按钮
                val searchInteractionSource = remember { MutableInteractionSource() }
                val searchIsPressed by searchInteractionSource.collectIsPressedAsState()
                val searchScale by animateFloatAsState(
                    targetValue = if (searchIsPressed) 0.95f else 1f,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                    label = "search_button_scale"
                )
                
                Button(
                    onClick = { onSearchLocally(song.title) },
                    modifier = Modifier
                        .weight(1f)
                        .scale(searchScale),
                    interactionSource = searchInteractionSource
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("本地搜索")
                }
            }
        }
    }
}

/**
 * 歌曲信息展示区域
 */
@Composable
private fun SongInfoSection(song: com.omar.musica.audiosearch.model.RecognizedSong) {
    Column {
        // 歌曲标题
        Text(
            text = song.title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // 艺术家
        Text(
            text = "演唱：${song.getArtistsString()}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
        )
        
        // 专辑（如果有）
        song.album?.let { album ->
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "专辑：$album",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 详细信息行
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 时长
            song.durationMs?.let { duration ->
                InfoChip(
                    icon = Icons.Default.Schedule,
                    text = song.getFormattedDuration()
                )
            }
            
            // 发布日期
            song.releaseDate?.let { date ->
                InfoChip(
                    icon = Icons.Default.DateRange,
                    text = date
                )
            }
        }
        
        // 流派标签
        song.genres?.takeIf { it.isNotEmpty() }?.let { genres ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "流派：${song.getGenresString()}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.6f)
            )
        }
    }
}

/**
 * 信息小标签
 */
@Composable
private fun InfoChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
        )
    }
}

/**
 * 识别失败的卡片
 */
@Composable
private fun RecognitionFailedCard(onRetryRecognition: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = "识别失败",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(48.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "未能识别此音乐",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onErrorContainer,
                fontWeight = FontWeight.Medium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "请尝试在更安静的环境中录制更清晰的音乐片段",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            val failedRetryInteractionSource = remember { MutableInteractionSource() }
            val failedRetryIsPressed by failedRetryInteractionSource.collectIsPressedAsState()
            val failedRetryScale by animateFloatAsState(
                targetValue = if (failedRetryIsPressed) 0.95f else 1f,
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                label = "failed_retry_button_scale"
            )
            
            Button(
                onClick = onRetryRecognition,
                modifier = Modifier.scale(failedRetryScale),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                interactionSource = failedRetryInteractionSource
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("重新识别")
            }
        }
    }
}