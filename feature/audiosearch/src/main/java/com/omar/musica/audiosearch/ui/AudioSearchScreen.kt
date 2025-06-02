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
 * Audio search main screen
 * 
 * Features:
 * 1. Recording button (microphone icon)
 * 2. Current recording status and duration
 * 3. Error messages (permission issues, etc.)
 * 4. Permission request handling
 * 5. Song recognition results
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioSearchScreen(
    modifier: Modifier = Modifier,
    viewModel: AudioSearchViewModel = hiltViewModel(),
    onNavigateToSearch: (String) -> Unit = {}
) {
    // Collect UI state
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    
    // Permission request launcher
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
        
        // Recording status indicator
        RecordingStatusIndicator(
            recordingStatus = uiState.recordingStatus,
            durationSeconds = uiState.recordingDurationSeconds
        )
        
        Spacer(modifier = Modifier.height(64.dp))
        
        // Main recording button
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
        
        Spacer(modifier = Modifier.height(48.dp))
        
        // Recognition status and results
        RecognitionStatusSection(
            recognitionStatus = uiState.recognitionStatus,
            recognizedSong = uiState.recognizedSong,
            recognitionProgressText = uiState.recognitionProgressText,
            onRetryRecognition = { viewModel.retryRecognition() },
            onRecordAgain = { viewModel.resetRecording() },
            onSearchLocally = onNavigateToSearch
        )
        
        // Error message display - avoid duplicate with recognition failed card
        // Don't show errorMessage when recognition status is FAILED, as we have a dedicated card for that
        if (uiState.recognitionStatus != RecognitionStatus.FAILED) {
            uiState.errorMessage?.let { errorMessage ->
                Spacer(modifier = Modifier.height(24.dp))
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
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

/**
 * Recording status indicator
 * Shows current recording status and duration
 */
@Composable
private fun RecordingStatusIndicator(
    recordingStatus: RecordingStatus,
    durationSeconds: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Status text
        val statusText = when (recordingStatus) {
            RecordingStatus.IDLE -> "Audio Search"
            RecordingStatus.RECORDING -> "Listening..."
            RecordingStatus.COMPLETED -> "Audio captured"
            RecordingStatus.ERROR -> "Recording failed"
        }
        
        val statusColor = when (recordingStatus) {
            RecordingStatus.IDLE -> MaterialTheme.colorScheme.onSurface
            RecordingStatus.RECORDING -> MaterialTheme.colorScheme.primary
            RecordingStatus.COMPLETED -> MaterialTheme.colorScheme.primary
            RecordingStatus.ERROR -> MaterialTheme.colorScheme.error
        }
        
        Text(
            text = statusText,
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = statusColor
        )
        
        // Recording duration (only shown while recording)
        if (recordingStatus == RecordingStatus.RECORDING) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = formatDuration(durationSeconds),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )
        } else if (recordingStatus == RecordingStatus.IDLE) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Tap to identify music",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

/**
 * Main recording button
 * Shows different buttons based on current state (record/stop/request permission)
 * Added dynamic effects: press scale animation and pulse effect during recording
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
    val buttonSize = 80.dp // Smaller, more elegant size
    
    // Interaction state
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    // Press scale animation
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "button_scale"
    )
    
    // Pulse animation during recording
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_animation")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )
    
    // Outer ring animation during recording
    val outerRingAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "outer_ring_alpha"
    )
    
    Box(
        modifier = Modifier.size(buttonSize * 1.4f), // Space for outer ring
        contentAlignment = Alignment.Center
    ) {
        // Outer ring effect during recording
        if (canStopRecording) {
            Box(
                modifier = Modifier
                    .size(buttonSize * 1.3f)
                    .scale(pulseScale)
                    .graphicsLayer {
                        alpha = outerRingAlpha
                    }
            ) {
                Card(
                    modifier = Modifier.fillMaxSize(),
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                    )
                ) {}
            }
        }
        
        when {
            // No permission - show permission request button
            !hasPermission -> {
                FilledTonalButton(
                    onClick = onRequestPermission,
                    modifier = Modifier
                        .size(buttonSize)
                        .scale(scale),
                    shape = CircleShape,
                    interactionSource = interactionSource
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Grant microphone permission",
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
            
            // Can stop recording - show stop button
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
                        contentDescription = "Stop recording",
                        modifier = Modifier.size(32.dp),
                        tint = Color.White
                    )
                }
            }
            
            // Can start recording - show record button
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
                        contentDescription = "Start recording",
                        modifier = Modifier.size(32.dp),
                        tint = Color.White
                    )
                }
            }
            
            // Other states - disabled button
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
                        contentDescription = "Recording button",
                        modifier = Modifier.size(32.dp)
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
 * Recognition status and results section
 */
@Composable
private fun RecognitionStatusSection(
    recognitionStatus: RecognitionStatus,
    recognizedSong: com.omar.musica.audiosearch.model.RecognizedSong?,
    recognitionProgressText: String?,
    onRetryRecognition: () -> Unit,
    onRecordAgain: () -> Unit,
    onSearchLocally: (String) -> Unit
) {
    when (recognitionStatus) {
        RecognitionStatus.IDLE -> {
            // Show nothing in idle state
        }
        
        RecognitionStatus.RECOGNIZING -> {
            // Recognition in progress
            RecognitionInProgressCard(progressText = recognitionProgressText)
        }
        
        RecognitionStatus.SUCCESS -> {
            // Recognition successful
            recognizedSong?.let { song ->
                RecognitionSuccessCard(
                    song = song,
                    onRetryRecognition = onRetryRecognition,
                    onSearchLocally = { onSearchLocally(song.title) }
                )
            }
        }
        
        RecognitionStatus.FAILED -> {
            // Recognition failed
            RecognitionFailedCard(onRecordAgain = onRecordAgain)
        }
    }
}

/**
 * Recognition in progress card
 */
@Composable
private fun RecognitionInProgressCard(progressText: String?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(40.dp),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 3.dp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = progressText ?: "Identifying music...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Recognition success result card
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
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Success header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Match found",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = "Song identified",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.SemiBold
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Song information
            SongInfoSection(song = song)
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Retry button
                OutlinedButton(
                    onClick = onRetryRecognition,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Retry")
                }
                
                // Search locally button
                Button(
                    onClick = { onSearchLocally(song.title) },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Find Local")
                }
            }
        }
    }
}

/**
 * Song information display section
 */
@Composable
private fun SongInfoSection(song: com.omar.musica.audiosearch.model.RecognizedSong) {
    Column {
        // Song title
        Text(
            text = song.title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // Artist
        Text(
            text = song.getArtistsString(),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.9f)
        )
        
        // Album (if available)
        song.album?.let { album ->
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = album,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
            )
        }
        
        // Additional info row
        song.durationMs?.let { duration ->
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = song.getFormattedDuration(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }
        }
    }
}

/**
 * Recognition failed card
 */
@Composable
private fun RecognitionFailedCard(onRecordAgain: () -> Unit) {
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
                contentDescription = "No match found",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(40.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "No match found",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onErrorContainer,
                fontWeight = FontWeight.Medium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Try recording in a quieter environment or closer to the audio source",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = onRecordAgain,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Record Again")
            }
        }
    }
}