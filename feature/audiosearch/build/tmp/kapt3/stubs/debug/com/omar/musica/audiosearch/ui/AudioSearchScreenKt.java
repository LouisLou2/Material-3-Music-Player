package com.omar.musica.audiosearch.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000L\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\u001a2\u0010\u0000\u001a\u00020\u00012\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\u0014\b\u0002\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u00010\u0007H\u0007\u001a\u0016\u0010\t\u001a\u00020\u00012\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00010\u000bH\u0003\u001a\u0012\u0010\f\u001a\u00020\u00012\b\u0010\r\u001a\u0004\u0018\u00010\bH\u0003\u001aT\u0010\u000e\u001a\u00020\u00012\u0006\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u00122\b\u0010\u0013\u001a\u0004\u0018\u00010\b2\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00010\u000b2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00010\u000b2\u0012\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u00010\u0007H\u0003\u001a2\u0010\u0016\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u00122\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00010\u000b2\u0012\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u00010\u0007H\u0003\u001aJ\u0010\u0018\u001a\u00020\u00012\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001a2\u0006\u0010\u001c\u001a\u00020\u001a2\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00010\u000b2\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u00010\u000b2\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u00010\u000bH\u0003\u001a\u0018\u0010 \u001a\u00020\u00012\u0006\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020$H\u0003\u001a\u0010\u0010%\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0012H\u0003\u001a\u0010\u0010&\u001a\u00020\b2\u0006\u0010\'\u001a\u00020$H\u0002\u00a8\u0006("}, d2 = {"AudioSearchScreen", "", "modifier", "Landroidx/compose/ui/Modifier;", "viewModel", "Lcom/omar/musica/audiosearch/ui/AudioSearchViewModel;", "onNavigateToSearch", "Lkotlin/Function1;", "", "RecognitionFailedCard", "onRecordAgain", "Lkotlin/Function0;", "RecognitionInProgressCard", "progressText", "RecognitionStatusSection", "recognitionStatus", "Lcom/omar/musica/audiosearch/ui/RecognitionStatus;", "recognizedSong", "Lcom/omar/musica/audiosearch/model/RecognizedSong;", "recognitionProgressText", "onRetryRecognition", "onSearchLocally", "RecognitionSuccessCard", "song", "RecordButton", "canStartRecording", "", "canStopRecording", "hasPermission", "onStartRecording", "onStopRecording", "onRequestPermission", "RecordingStatusIndicator", "recordingStatus", "Lcom/omar/musica/audiosearch/data/recorder/RecordingStatus;", "durationSeconds", "", "SongInfoSection", "formatDuration", "seconds", "audiosearch_debug"})
public final class AudioSearchScreenKt {
    
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
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable
    public static final void AudioSearchScreen(@org.jetbrains.annotations.NotNull
    androidx.compose.ui.Modifier modifier, @org.jetbrains.annotations.NotNull
    com.omar.musica.audiosearch.ui.AudioSearchViewModel viewModel, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onNavigateToSearch) {
    }
    
    /**
     * Recording status indicator
     * Shows current recording status and duration
     */
    @androidx.compose.runtime.Composable
    private static final void RecordingStatusIndicator(com.omar.musica.audiosearch.data.recorder.RecordingStatus recordingStatus, int durationSeconds) {
    }
    
    /**
     * Main recording button
     * Shows different buttons based on current state (record/stop/request permission)
     * Added dynamic effects: press scale animation and pulse effect during recording
     */
    @androidx.compose.runtime.Composable
    private static final void RecordButton(boolean canStartRecording, boolean canStopRecording, boolean hasPermission, kotlin.jvm.functions.Function0<kotlin.Unit> onStartRecording, kotlin.jvm.functions.Function0<kotlin.Unit> onStopRecording, kotlin.jvm.functions.Function0<kotlin.Unit> onRequestPermission) {
    }
    
    /**
     * 格式化录制时长为 MM:SS 格式
     */
    private static final java.lang.String formatDuration(int seconds) {
        return null;
    }
    
    /**
     * Recognition status and results section
     */
    @androidx.compose.runtime.Composable
    private static final void RecognitionStatusSection(com.omar.musica.audiosearch.ui.RecognitionStatus recognitionStatus, com.omar.musica.audiosearch.model.RecognizedSong recognizedSong, java.lang.String recognitionProgressText, kotlin.jvm.functions.Function0<kotlin.Unit> onRetryRecognition, kotlin.jvm.functions.Function0<kotlin.Unit> onRecordAgain, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onSearchLocally) {
    }
    
    /**
     * Recognition in progress card
     */
    @androidx.compose.runtime.Composable
    private static final void RecognitionInProgressCard(java.lang.String progressText) {
    }
    
    /**
     * Recognition success result card
     */
    @androidx.compose.runtime.Composable
    private static final void RecognitionSuccessCard(com.omar.musica.audiosearch.model.RecognizedSong song, kotlin.jvm.functions.Function0<kotlin.Unit> onRetryRecognition, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onSearchLocally) {
    }
    
    /**
     * Song information display section
     */
    @androidx.compose.runtime.Composable
    private static final void SongInfoSection(com.omar.musica.audiosearch.model.RecognizedSong song) {
    }
    
    /**
     * Recognition failed card
     */
    @androidx.compose.runtime.Composable
    private static final void RecognitionFailedCard(kotlin.jvm.functions.Function0<kotlin.Unit> onRecordAgain) {
    }
}