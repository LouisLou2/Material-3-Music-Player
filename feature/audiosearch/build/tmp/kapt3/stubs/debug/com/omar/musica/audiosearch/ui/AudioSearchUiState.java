package com.omar.musica.audiosearch.ui;

/**
 * 听歌识曲页面的UI状态
 *
 * 包含录制状态、权限状态、识别结果等所有UI需要的信息
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b&\b\u0086\b\u0018\u00002\u00020\u0001B[\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0007\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\f\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u000e\u0012\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u000e\u00a2\u0006\u0002\u0010\u0010J\t\u0010\'\u001a\u00020\u0003H\u00c6\u0003J\t\u0010(\u001a\u00020\u0005H\u00c6\u0003J\t\u0010)\u001a\u00020\u0007H\u00c6\u0003J\t\u0010*\u001a\u00020\u0007H\u00c6\u0003J\t\u0010+\u001a\u00020\nH\u00c6\u0003J\u000b\u0010,\u001a\u0004\u0018\u00010\fH\u00c6\u0003J\u000b\u0010-\u001a\u0004\u0018\u00010\u000eH\u00c6\u0003J\u000b\u0010.\u001a\u0004\u0018\u00010\u000eH\u00c6\u0003J_\u0010/\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00072\b\b\u0002\u0010\t\u001a\u00020\n2\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\f2\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u000e2\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u000eH\u00c6\u0001J\u0013\u00100\u001a\u00020\u00072\b\u00101\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u00102\u001a\u00020\u0005H\u00d6\u0001J\t\u00103\u001a\u00020\u000eH\u00d6\u0001R\u0011\u0010\u0011\u001a\u00020\u00078F\u00a2\u0006\u0006\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\u0014\u001a\u00020\u00078F\u00a2\u0006\u0006\u001a\u0004\b\u0015\u0010\u0013R\u0011\u0010\u0016\u001a\u00020\u00078F\u00a2\u0006\u0006\u001a\u0004\b\u0017\u0010\u0013R\u0013\u0010\u000f\u001a\u0004\u0018\u00010\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0011\u0010\u001a\u001a\u00020\u00078F\u00a2\u0006\u0006\u001a\u0004\b\u001b\u0010\u0013R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0013R\u0011\u0010\b\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0013R\u0011\u0010\u001d\u001a\u00020\u00078F\u00a2\u0006\u0006\u001a\u0004\b\u001d\u0010\u0013R\u0013\u0010\r\u001a\u0004\u0018\u00010\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0019R\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 R\u0013\u0010\u000b\u001a\u0004\u0018\u00010\f\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\"R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010$R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010&\u00a8\u00064"}, d2 = {"Lcom/omar/musica/audiosearch/ui/AudioSearchUiState;", "", "recordingStatus", "Lcom/omar/musica/audiosearch/data/recorder/RecordingStatus;", "recordingDurationSeconds", "", "hasRecordPermission", "", "isCheckingPermission", "recognitionStatus", "Lcom/omar/musica/audiosearch/ui/RecognitionStatus;", "recognizedSong", "Lcom/omar/musica/audiosearch/model/RecognizedSong;", "recognitionProgressText", "", "errorMessage", "(Lcom/omar/musica/audiosearch/data/recorder/RecordingStatus;IZZLcom/omar/musica/audiosearch/ui/RecognitionStatus;Lcom/omar/musica/audiosearch/model/RecognizedSong;Ljava/lang/String;Ljava/lang/String;)V", "canStartRecognition", "getCanStartRecognition", "()Z", "canStartRecording", "getCanStartRecording", "canStopRecording", "getCanStopRecording", "getErrorMessage", "()Ljava/lang/String;", "hasRecognitionResult", "getHasRecognitionResult", "getHasRecordPermission", "isProcessing", "getRecognitionProgressText", "getRecognitionStatus", "()Lcom/omar/musica/audiosearch/ui/RecognitionStatus;", "getRecognizedSong", "()Lcom/omar/musica/audiosearch/model/RecognizedSong;", "getRecordingDurationSeconds", "()I", "getRecordingStatus", "()Lcom/omar/musica/audiosearch/data/recorder/RecordingStatus;", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "copy", "equals", "other", "hashCode", "toString", "audiosearch_debug"})
public final class AudioSearchUiState {
    @org.jetbrains.annotations.NotNull
    private final com.omar.musica.audiosearch.data.recorder.RecordingStatus recordingStatus = null;
    private final int recordingDurationSeconds = 0;
    private final boolean hasRecordPermission = false;
    private final boolean isCheckingPermission = false;
    @org.jetbrains.annotations.NotNull
    private final com.omar.musica.audiosearch.ui.RecognitionStatus recognitionStatus = null;
    @org.jetbrains.annotations.Nullable
    private final com.omar.musica.audiosearch.model.RecognizedSong recognizedSong = null;
    @org.jetbrains.annotations.Nullable
    private final java.lang.String recognitionProgressText = null;
    @org.jetbrains.annotations.Nullable
    private final java.lang.String errorMessage = null;
    
    public AudioSearchUiState(@org.jetbrains.annotations.NotNull
    com.omar.musica.audiosearch.data.recorder.RecordingStatus recordingStatus, int recordingDurationSeconds, boolean hasRecordPermission, boolean isCheckingPermission, @org.jetbrains.annotations.NotNull
    com.omar.musica.audiosearch.ui.RecognitionStatus recognitionStatus, @org.jetbrains.annotations.Nullable
    com.omar.musica.audiosearch.model.RecognizedSong recognizedSong, @org.jetbrains.annotations.Nullable
    java.lang.String recognitionProgressText, @org.jetbrains.annotations.Nullable
    java.lang.String errorMessage) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.omar.musica.audiosearch.data.recorder.RecordingStatus getRecordingStatus() {
        return null;
    }
    
    public final int getRecordingDurationSeconds() {
        return 0;
    }
    
    public final boolean getHasRecordPermission() {
        return false;
    }
    
    public final boolean isCheckingPermission() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.omar.musica.audiosearch.ui.RecognitionStatus getRecognitionStatus() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.omar.musica.audiosearch.model.RecognizedSong getRecognizedSong() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getRecognitionProgressText() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getErrorMessage() {
        return null;
    }
    
    public final boolean getCanStartRecording() {
        return false;
    }
    
    public final boolean getCanStopRecording() {
        return false;
    }
    
    public final boolean getCanStartRecognition() {
        return false;
    }
    
    public final boolean isProcessing() {
        return false;
    }
    
    public final boolean getHasRecognitionResult() {
        return false;
    }
    
    public AudioSearchUiState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.omar.musica.audiosearch.data.recorder.RecordingStatus component1() {
        return null;
    }
    
    public final int component2() {
        return 0;
    }
    
    public final boolean component3() {
        return false;
    }
    
    public final boolean component4() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.omar.musica.audiosearch.ui.RecognitionStatus component5() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.omar.musica.audiosearch.model.RecognizedSong component6() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component7() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component8() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.omar.musica.audiosearch.ui.AudioSearchUiState copy(@org.jetbrains.annotations.NotNull
    com.omar.musica.audiosearch.data.recorder.RecordingStatus recordingStatus, int recordingDurationSeconds, boolean hasRecordPermission, boolean isCheckingPermission, @org.jetbrains.annotations.NotNull
    com.omar.musica.audiosearch.ui.RecognitionStatus recognitionStatus, @org.jetbrains.annotations.Nullable
    com.omar.musica.audiosearch.model.RecognizedSong recognizedSong, @org.jetbrains.annotations.Nullable
    java.lang.String recognitionProgressText, @org.jetbrains.annotations.Nullable
    java.lang.String errorMessage) {
        return null;
    }
    
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public java.lang.String toString() {
        return null;
    }
}