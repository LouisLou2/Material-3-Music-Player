package com.omar.musica.audiosearch.ui;

/**
 * 听歌识曲界面的UI状态
 *
 * 这个类定义了界面需要显示的所有信息：
 * - 录制状态（是否在录音、是否完成等）
 * - 错误信息（权限问题、录制失败等）
 * - 是否有录制权限
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0019\b\u0086\b\u0018\u00002\u00020\u0001B9\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u0012\b\b\u0002\u0010\n\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\u000bJ\t\u0010\u0018\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010\u0019\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\t\u0010\u001a\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u001b\u001a\u00020\tH\u00c6\u0003J\t\u0010\u001c\u001a\u00020\u0007H\u00c6\u0003J=\u0010\u001d\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u0007H\u00c6\u0001J\u0013\u0010\u001e\u001a\u00020\u00072\b\u0010\u001f\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010 \u001a\u00020\tH\u00d6\u0001J\t\u0010!\u001a\u00020\u0005H\u00d6\u0001R\u0011\u0010\f\u001a\u00020\u00078F\u00a2\u0006\u0006\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u000f\u001a\u00020\u00078F\u00a2\u0006\u0006\u001a\u0004\b\u0010\u0010\u000eR\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u000eR\u0011\u0010\n\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000eR\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017\u00a8\u0006\""}, d2 = {"Lcom/omar/musica/audiosearch/ui/AudioSearchUiState;", "", "recordingStatus", "Lcom/omar/musica/audiosearch/data/recorder/RecordingStatus;", "errorMessage", "", "hasRecordPermission", "", "recordingDurationSeconds", "", "isCheckingPermission", "(Lcom/omar/musica/audiosearch/data/recorder/RecordingStatus;Ljava/lang/String;ZIZ)V", "canStartRecording", "getCanStartRecording", "()Z", "canStopRecording", "getCanStopRecording", "getErrorMessage", "()Ljava/lang/String;", "getHasRecordPermission", "getRecordingDurationSeconds", "()I", "getRecordingStatus", "()Lcom/omar/musica/audiosearch/data/recorder/RecordingStatus;", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "other", "hashCode", "toString", "audiosearch_debug"})
public final class AudioSearchUiState {
    
    /**
     * 当前录制状态
     */
    @org.jetbrains.annotations.NotNull
    private final com.omar.musica.audiosearch.data.recorder.RecordingStatus recordingStatus = null;
    
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
    
    public AudioSearchUiState(@org.jetbrains.annotations.NotNull
    com.omar.musica.audiosearch.data.recorder.RecordingStatus recordingStatus, @org.jetbrains.annotations.Nullable
    java.lang.String errorMessage, boolean hasRecordPermission, int recordingDurationSeconds, boolean isCheckingPermission) {
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
    
    public final boolean getCanStartRecording() {
        return false;
    }
    
    public final boolean getCanStopRecording() {
        return false;
    }
    
    public AudioSearchUiState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.omar.musica.audiosearch.data.recorder.RecordingStatus component1() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component2() {
        return null;
    }
    
    public final boolean component3() {
        return false;
    }
    
    public final int component4() {
        return 0;
    }
    
    public final boolean component5() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.omar.musica.audiosearch.ui.AudioSearchUiState copy(@org.jetbrains.annotations.NotNull
    com.omar.musica.audiosearch.data.recorder.RecordingStatus recordingStatus, @org.jetbrains.annotations.Nullable
    java.lang.String errorMessage, boolean hasRecordPermission, int recordingDurationSeconds, boolean isCheckingPermission) {
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