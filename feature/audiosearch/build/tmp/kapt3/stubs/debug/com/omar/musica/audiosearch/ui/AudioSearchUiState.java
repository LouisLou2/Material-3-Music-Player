package com.omar.musica.audiosearch.ui;

/**
 * 听歌识曲界面的UI状态
 *
 * 这个类定义了界面需要显示的所有信息：
 * - 录制状态（是否在录音、是否完成等）
 * - 识别状态（是否在识别、识别结果等）
 * - 错误信息（权限问题、录制失败等）
 * - 是否有录制权限
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b&\b\u0086\b\u0018\u00002\u00020\u0001B[\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b\u0012\b\b\u0002\u0010\f\u001a\u00020\t\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u000e\u0012\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\u0002\u0010\u0010J\t\u0010\'\u001a\u00020\u0003H\u00c6\u0003J\t\u0010(\u001a\u00020\u0005H\u00c6\u0003J\u000b\u0010)\u001a\u0004\u0018\u00010\u0007H\u00c6\u0003J\t\u0010*\u001a\u00020\tH\u00c6\u0003J\t\u0010+\u001a\u00020\u000bH\u00c6\u0003J\t\u0010,\u001a\u00020\tH\u00c6\u0003J\u000b\u0010-\u001a\u0004\u0018\u00010\u000eH\u00c6\u0003J\u000b\u0010.\u001a\u0004\u0018\u00010\u0007H\u00c6\u0003J_\u0010/\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00072\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\t2\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u000e2\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0007H\u00c6\u0001J\u0013\u00100\u001a\u00020\t2\b\u00101\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u00102\u001a\u00020\u000bH\u00d6\u0001J\t\u00103\u001a\u00020\u0007H\u00d6\u0001R\u0011\u0010\u0011\u001a\u00020\t8F\u00a2\u0006\u0006\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\u0014\u001a\u00020\t8F\u00a2\u0006\u0006\u001a\u0004\b\u0015\u0010\u0013R\u0011\u0010\u0016\u001a\u00020\t8F\u00a2\u0006\u0006\u001a\u0004\b\u0017\u0010\u0013R\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0011\u0010\u001a\u001a\u00020\t8F\u00a2\u0006\u0006\u001a\u0004\b\u001b\u0010\u0013R\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0013R\u0011\u0010\f\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u0013R\u0011\u0010\u001d\u001a\u00020\t8F\u00a2\u0006\u0006\u001a\u0004\b\u001d\u0010\u0013R\u0013\u0010\u000f\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0019R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 R\u0013\u0010\r\u001a\u0004\u0018\u00010\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\"R\u0011\u0010\n\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010$R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010&\u00a8\u00064"}, d2 = {"Lcom/omar/musica/audiosearch/ui/AudioSearchUiState;", "", "recordingStatus", "Lcom/omar/musica/audiosearch/data/recorder/RecordingStatus;", "recognitionStatus", "Lcom/omar/musica/audiosearch/ui/RecognitionStatus;", "errorMessage", "", "hasRecordPermission", "", "recordingDurationSeconds", "", "isCheckingPermission", "recognizedSong", "Lcom/omar/musica/audiosearch/model/RecognizedSong;", "recognitionProgressText", "(Lcom/omar/musica/audiosearch/data/recorder/RecordingStatus;Lcom/omar/musica/audiosearch/ui/RecognitionStatus;Ljava/lang/String;ZIZLcom/omar/musica/audiosearch/model/RecognizedSong;Ljava/lang/String;)V", "canStartRecognition", "getCanStartRecognition", "()Z", "canStartRecording", "getCanStartRecording", "canStopRecording", "getCanStopRecording", "getErrorMessage", "()Ljava/lang/String;", "hasRecognitionResult", "getHasRecognitionResult", "getHasRecordPermission", "isProcessing", "getRecognitionProgressText", "getRecognitionStatus", "()Lcom/omar/musica/audiosearch/ui/RecognitionStatus;", "getRecognizedSong", "()Lcom/omar/musica/audiosearch/model/RecognizedSong;", "getRecordingDurationSeconds", "()I", "getRecordingStatus", "()Lcom/omar/musica/audiosearch/data/recorder/RecordingStatus;", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "copy", "equals", "other", "hashCode", "toString", "audiosearch_debug"})
public final class AudioSearchUiState {
    
    /**
     * 当前录制状态
     */
    @org.jetbrains.annotations.NotNull
    private final com.omar.musica.audiosearch.data.recorder.RecordingStatus recordingStatus = null;
    
    /**
     * 当前识别状态
     */
    @org.jetbrains.annotations.NotNull
    private final com.omar.musica.audiosearch.ui.RecognitionStatus recognitionStatus = null;
    
    /**
     * 错误消息，如果有错误则显示给用户
     */
    @org.jetbrains.annotations.Nullable
    private final java.lang.String errorMessage = null;
    
    /**
     * 是否有录制音频的权限
     */
    private final boolean hasRecordPermission = false;
    
    /**
     * 录制时长（秒） - 用于显示录制进度
     */
    private final int recordingDurationSeconds = 0;
    
    /**
     * 是否正在检查权限
     */
    private final boolean isCheckingPermission = false;
    
    /**
     * 识别到的歌曲信息
     */
    @org.jetbrains.annotations.Nullable
    private final com.omar.musica.audiosearch.model.RecognizedSong recognizedSong = null;
    
    /**
     * 识别进度文本
     */
    @org.jetbrains.annotations.Nullable
    private final java.lang.String recognitionProgressText = null;
    
    public AudioSearchUiState(@org.jetbrains.annotations.NotNull
    com.omar.musica.audiosearch.data.recorder.RecordingStatus recordingStatus, @org.jetbrains.annotations.NotNull
    com.omar.musica.audiosearch.ui.RecognitionStatus recognitionStatus, @org.jetbrains.annotations.Nullable
    java.lang.String errorMessage, boolean hasRecordPermission, int recordingDurationSeconds, boolean isCheckingPermission, @org.jetbrains.annotations.Nullable
    com.omar.musica.audiosearch.model.RecognizedSong recognizedSong, @org.jetbrains.annotations.Nullable
    java.lang.String recognitionProgressText) {
        super();
    }
    
    /**
     * 当前录制状态
     */
    @org.jetbrains.annotations.NotNull
    public final com.omar.musica.audiosearch.data.recorder.RecordingStatus getRecordingStatus() {
        return null;
    }
    
    /**
     * 当前识别状态
     */
    @org.jetbrains.annotations.NotNull
    public final com.omar.musica.audiosearch.ui.RecognitionStatus getRecognitionStatus() {
        return null;
    }
    
    /**
     * 错误消息，如果有错误则显示给用户
     */
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getErrorMessage() {
        return null;
    }
    
    /**
     * 是否有录制音频的权限
     */
    public final boolean getHasRecordPermission() {
        return false;
    }
    
    /**
     * 录制时长（秒） - 用于显示录制进度
     */
    public final int getRecordingDurationSeconds() {
        return 0;
    }
    
    /**
     * 是否正在检查权限
     */
    public final boolean isCheckingPermission() {
        return false;
    }
    
    /**
     * 识别到的歌曲信息
     */
    @org.jetbrains.annotations.Nullable
    public final com.omar.musica.audiosearch.model.RecognizedSong getRecognizedSong() {
        return null;
    }
    
    /**
     * 识别进度文本
     */
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getRecognitionProgressText() {
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
    
    @org.jetbrains.annotations.NotNull
    public final com.omar.musica.audiosearch.ui.RecognitionStatus component2() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component3() {
        return null;
    }
    
    public final boolean component4() {
        return false;
    }
    
    public final int component5() {
        return 0;
    }
    
    public final boolean component6() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.omar.musica.audiosearch.model.RecognizedSong component7() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component8() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.omar.musica.audiosearch.ui.AudioSearchUiState copy(@org.jetbrains.annotations.NotNull
    com.omar.musica.audiosearch.data.recorder.RecordingStatus recordingStatus, @org.jetbrains.annotations.NotNull
    com.omar.musica.audiosearch.ui.RecognitionStatus recognitionStatus, @org.jetbrains.annotations.Nullable
    java.lang.String errorMessage, boolean hasRecordPermission, int recordingDurationSeconds, boolean isCheckingPermission, @org.jetbrains.annotations.Nullable
    com.omar.musica.audiosearch.model.RecognizedSong recognizedSong, @org.jetbrains.annotations.Nullable
    java.lang.String recognitionProgressText) {
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