package com.omar.musica.audiosearch.data.recorder;

/**
 * 音频录制器核心类
 * 负责管理 Android 系统的音频录制功能
 *
 * 技术说明：
 * - 使用 AudioRecord 进行低级别的音频录制
 * - 自动尝试不同的音频源以提高兼容性
 * - 管理音频焦点确保录制质量
 * - 配置为 44.1kHz 采样率，16位深度，单声道
 * - 录制的音频数据存储在内存中的字节数组
 */
@javax.inject.Singleton
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000l\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\t\b\u0007\u0018\u0000 32\u00020\u0001:\u000223B\u0011\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010 \u001a\u00020!H\u0002J\b\u0010\"\u001a\u00020!H\u0002J\b\u0010#\u001a\u00020$H\u0002J\u0010\u0010%\u001a\u00020&2\u0006\u0010\'\u001a\u00020\u0012H\u0002J\b\u0010(\u001a\u0004\u0018\u00010\tJ\b\u0010)\u001a\u00020!H\u0002J\b\u0010*\u001a\u00020+H\u0002J\u0006\u0010,\u001a\u00020!J\u0006\u0010-\u001a\u00020!J\b\u0010.\u001a\u00020!H\u0002J\u0006\u0010/\u001a\u00020!J\u0018\u00100\u001a\u00020$2\u0006\u00101\u001a\u00020\u000f2\u0006\u0010\'\u001a\u00020\u0012H\u0002R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0013\u001a\u00020\u00128BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0016\u0010\u0017\u001a\u0004\b\u0014\u0010\u0015R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u001a\u001a\u0004\u0018\u00010\u001bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00070\u001d\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001f\u00a8\u00064"}, d2 = {"Lcom/omar/musica/audiosearch/data/recorder/AudioRecorder;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "_recordingStatus", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/omar/musica/audiosearch/data/recorder/RecordingStatus;", "audioData", "", "audioFocusRequest", "Landroid/media/AudioFocusRequest;", "audioManager", "Landroid/media/AudioManager;", "audioRecord", "Landroid/media/AudioRecord;", "audioSources", "", "", "bufferSize", "getBufferSize", "()I", "bufferSize$delegate", "Lkotlin/Lazy;", "currentAudioSource", "originalAudioMode", "recordingJob", "Lkotlinx/coroutines/Job;", "recordingStatus", "Lkotlinx/coroutines/flow/StateFlow;", "getRecordingStatus", "()Lkotlinx/coroutines/flow/StateFlow;", "cleanupAudioRecord", "", "configureAudioManager", "createAudioRecord", "Lcom/omar/musica/audiosearch/data/recorder/AudioRecorder$AudioRecordResult;", "getAudioSourceName", "", "audioSource", "getRecordedAudioData", "releaseAudioFocus", "requestAudioFocus", "", "reset", "startRecording", "startRecordingInBackground", "stopRecording", "testRecordingCapability", "record", "AudioRecordResult", "Companion", "audiosearch_debug"})
public final class AudioRecorder {
    @org.jetbrains.annotations.NotNull
    private final android.content.Context context = null;
    @java.lang.Deprecated
    public static final int SAMPLE_RATE = 44100;
    @java.lang.Deprecated
    public static final int CHANNEL_CONFIG = android.media.AudioFormat.CHANNEL_IN_MONO;
    @java.lang.Deprecated
    public static final int AUDIO_FORMAT = android.media.AudioFormat.ENCODING_PCM_16BIT;
    @java.lang.Deprecated
    public static final long RECORDING_TIMEOUT_MS = 30000L;
    @java.lang.Deprecated
    public static final long INITIALIZATION_DELAY_MS = 200L;
    @java.lang.Deprecated
    public static final long READ_DELAY_MS = 10L;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<com.omar.musica.audiosearch.data.recorder.RecordingStatus> _recordingStatus = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.omar.musica.audiosearch.data.recorder.RecordingStatus> recordingStatus = null;
    @org.jetbrains.annotations.Nullable
    private android.media.AudioRecord audioRecord;
    @org.jetbrains.annotations.Nullable
    private kotlinx.coroutines.Job recordingJob;
    @org.jetbrains.annotations.Nullable
    private byte[] audioData;
    private int currentAudioSource = android.media.MediaRecorder.AudioSource.MIC;
    @org.jetbrains.annotations.NotNull
    private final android.media.AudioManager audioManager = null;
    @org.jetbrains.annotations.Nullable
    private android.media.AudioFocusRequest audioFocusRequest;
    private int originalAudioMode = android.media.AudioManager.MODE_NORMAL;
    @org.jetbrains.annotations.NotNull
    private final java.util.List<java.lang.Integer> audioSources = null;
    @org.jetbrains.annotations.NotNull
    private final kotlin.Lazy bufferSize$delegate = null;
    @org.jetbrains.annotations.NotNull
    private static final com.omar.musica.audiosearch.data.recorder.AudioRecorder.Companion Companion = null;
    
    @javax.inject.Inject
    public AudioRecorder(@dagger.hilt.android.qualifiers.ApplicationContext
    @org.jetbrains.annotations.NotNull
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.omar.musica.audiosearch.data.recorder.RecordingStatus> getRecordingStatus() {
        return null;
    }
    
    private final int getBufferSize() {
        return 0;
    }
    
    /**
     * 开始录音
     *
     * 工作流程：
     * 1. 检查当前状态，避免重复录制
     * 2. 请求音频焦点
     * 3. 配置音频管理器
     * 4. 尝试不同的音频源创建 AudioRecord 对象
     * 5. 启动后台录制任务
     * 6. 更新状态为 RECORDING
     */
    public final void startRecording() {
    }
    
    /**
     * 请求音频焦点
     */
    private final boolean requestAudioFocus() {
        return false;
    }
    
    /**
     * 配置音频管理器
     */
    private final void configureAudioManager() {
    }
    
    /**
     * 释放音频焦点
     */
    private final void releaseAudioFocus() {
    }
    
    /**
     * 尝试创建 AudioRecord 对象
     * 按优先级尝试不同的音频源
     */
    private final com.omar.musica.audiosearch.data.recorder.AudioRecorder.AudioRecordResult createAudioRecord() {
        return null;
    }
    
    /**
     * 测试录制能力
     */
    private final com.omar.musica.audiosearch.data.recorder.AudioRecorder.AudioRecordResult testRecordingCapability(android.media.AudioRecord record, int audioSource) {
        return null;
    }
    
    /**
     * 获取音频源名称
     */
    private final java.lang.String getAudioSourceName(int audioSource) {
        return null;
    }
    
    /**
     * 停止录音
     *
     * 工作流程：
     * 1. 更新状态让后台任务知道要停止
     * 2. 停止 AudioRecord 的录制
     * 3. 等待后台任务完成数据保存
     * 4. 释放音频焦点
     * 5. 清理资源
     */
    public final void stopRecording() {
    }
    
    /**
     * 获取录制的音频数据
     *
     * @return 录制的音频字节数组，如果没有录制数据则返回null
     */
    @org.jetbrains.annotations.Nullable
    public final byte[] getRecordedAudioData() {
        return null;
    }
    
    /**
     * 重置录制器状态
     * 清除之前的录制数据，准备新的录制
     */
    public final void reset() {
    }
    
    /**
     * 清理AudioRecord资源
     */
    private final void cleanupAudioRecord() {
    }
    
    /**
     * 后台录制任务
     * 在协程中持续读取音频数据并存储到内存
     */
    private final void startRecordingInBackground() {
    }
    
    /**
     * AudioRecord 创建结果
     */
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\n\n\u0002\u0010\b\n\u0002\b\u0002\b\u0082\b\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\u0002\u0010\u0006J\t\u0010\n\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010\u000b\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u001f\u0010\f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u00c6\u0001J\u0013\u0010\r\u001a\u00020\u00032\b\u0010\u000e\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u000f\u001a\u00020\u0010H\u00d6\u0001J\t\u0010\u0011\u001a\u00020\u0005H\u00d6\u0001R\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\t\u00a8\u0006\u0012"}, d2 = {"Lcom/omar/musica/audiosearch/data/recorder/AudioRecorder$AudioRecordResult;", "", "isSuccess", "", "errorMessage", "", "(ZLjava/lang/String;)V", "getErrorMessage", "()Ljava/lang/String;", "()Z", "component1", "component2", "copy", "equals", "other", "hashCode", "", "toString", "audiosearch_debug"})
    static final class AudioRecordResult {
        private final boolean isSuccess = false;
        @org.jetbrains.annotations.Nullable
        private final java.lang.String errorMessage = null;
        
        public AudioRecordResult(boolean isSuccess, @org.jetbrains.annotations.Nullable
        java.lang.String errorMessage) {
            super();
        }
        
        public final boolean isSuccess() {
            return false;
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.lang.String getErrorMessage() {
            return null;
        }
        
        public final boolean component1() {
            return false;
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.lang.String component2() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.omar.musica.audiosearch.data.recorder.AudioRecorder.AudioRecordResult copy(boolean isSuccess, @org.jetbrains.annotations.Nullable
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0004\b\u0082\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0007X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2 = {"Lcom/omar/musica/audiosearch/data/recorder/AudioRecorder$Companion;", "", "()V", "AUDIO_FORMAT", "", "CHANNEL_CONFIG", "INITIALIZATION_DELAY_MS", "", "READ_DELAY_MS", "RECORDING_TIMEOUT_MS", "SAMPLE_RATE", "audiosearch_debug"})
    static final class Companion {
        
        private Companion() {
            super();
        }
    }
}