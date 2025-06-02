package com.omar.musica.audiosearch.ui;

/**
 * 听歌识曲功能的ViewModel
 *
 * 职责：
 * 1. 管理UI状态（录制状态、权限状态、错误信息等）
 * 2. 处理用户操作（开始录音、停止录音、开始识别）
 * 3. 权限管理（检查和请求录音权限）
 * 4. 与AudioRecorder和AudioRecognitionSource协调工作
 * 5. 计算录制时长并更新UI
 * 6. 保存录制的音频文件用于调试
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B!\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\b\u0010\u0013\u001a\u00020\u0014H\u0002J\b\u0010\u0015\u001a\u00020\u0014H\u0002J\u0006\u0010\u0016\u001a\u00020\u0014J\u0010\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0082@\u00a2\u0006\u0002\u0010\u0019J\u0010\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001dH\u0002J\b\u0010\u001e\u001a\u00020\u0014H\u0002J\b\u0010\u001f\u001a\u00020\u0014H\u0014J\u000e\u0010 \u001a\u00020\u00142\u0006\u0010!\u001a\u00020\"J\u0012\u0010#\u001a\u0004\u0018\u00010$2\u0006\u0010%\u001a\u00020&H\u0002J\b\u0010\'\u001a\u00020\u0014H\u0002J\u0006\u0010(\u001a\u00020\u0014J\u0006\u0010)\u001a\u00020\u0014J\u0010\u0010*\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001dH\u0002J\b\u0010+\u001a\u00020\u0014H\u0002J\b\u0010,\u001a\u00020\u0014H\u0002J\u0006\u0010-\u001a\u00020\u0014J\b\u0010.\u001a\u00020\u0014H\u0002J\u0006\u0010/\u001a\u00020\u0014J\u0010\u00100\u001a\u00020\u00142\u0006\u00101\u001a\u000202H\u0002J\u0010\u00103\u001a\u00020\u00142\u0006\u00104\u001a\u000205H\u0002J\u0018\u00106\u001a\u00020\u00142\u0006\u00107\u001a\u00020\u00182\u0006\u00108\u001a\u00020\u001bH\u0002R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u0004\u0018\u00010\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012\u00a8\u00069"}, d2 = {"Lcom/omar/musica/audiosearch/ui/AudioSearchViewModel;", "Landroidx/lifecycle/ViewModel;", "context", "Landroid/content/Context;", "audioRecorder", "Lcom/omar/musica/audiosearch/data/recorder/AudioRecorder;", "audioRecognitionSource", "Lcom/omar/musica/network/data/AudioRecognitionSource;", "(Landroid/content/Context;Lcom/omar/musica/audiosearch/data/recorder/AudioRecorder;Lcom/omar/musica/network/data/AudioRecognitionSource;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/omar/musica/audiosearch/ui/AudioSearchUiState;", "durationTimerJob", "Lkotlinx/coroutines/Job;", "recognitionJob", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "checkApiConfiguration", "", "checkRecordPermission", "clearError", "createTempAudioFile", "Ljava/io/File;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "intToByteArray", "", "value", "", "observeRecordingStatus", "onCleared", "onPermissionResult", "granted", "", "parseRecognitionResponse", "Lcom/omar/musica/audiosearch/model/RecognizedSong;", "response", "Lcom/omar/musica/network/model/AudioRecognitionResponse;", "resetRecognitionState", "resetRecording", "retryRecognition", "shortToByteArray", "startAudioRecognition", "startDurationTimer", "startRecording", "stopDurationTimer", "stopRecording", "updateErrorMessage", "message", "", "updateRecognitionStatus", "status", "Lcom/omar/musica/audiosearch/ui/RecognitionStatus;", "writeWavFile", "file", "pcmData", "audiosearch_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel
public final class AudioSearchViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull
    private final com.omar.musica.audiosearch.data.recorder.AudioRecorder audioRecorder = null;
    @org.jetbrains.annotations.NotNull
    private final com.omar.musica.network.data.AudioRecognitionSource audioRecognitionSource = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<com.omar.musica.audiosearch.ui.AudioSearchUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.omar.musica.audiosearch.ui.AudioSearchUiState> uiState = null;
    @org.jetbrains.annotations.Nullable
    private kotlinx.coroutines.Job durationTimerJob;
    @org.jetbrains.annotations.Nullable
    private kotlinx.coroutines.Job recognitionJob;
    
    @javax.inject.Inject
    public AudioSearchViewModel(@dagger.hilt.android.qualifiers.ApplicationContext
    @org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    com.omar.musica.audiosearch.data.recorder.AudioRecorder audioRecorder, @org.jetbrains.annotations.NotNull
    com.omar.musica.network.data.AudioRecognitionSource audioRecognitionSource) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.omar.musica.audiosearch.ui.AudioSearchUiState> getUiState() {
        return null;
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
    public final void startRecording() {
    }
    
    /**
     * 停止录音
     *
     * 流程：
     * 1. 停止AudioRecorder
     * 2. 停止计时器
     * 3. 获取录制的音频数据
     * 4. 保存音频文件用于调试
     * 5. 自动开始识别
     */
    public final void stopRecording() {
    }
    
    /**
     * 开始音频识别
     */
    private final void startAudioRecognition() {
    }
    
    /**
     * 创建临时音频文件
     * 将原始PCM数据转换为带有正确WAV头的音频文件
     */
    private final java.lang.Object createTempAudioFile(kotlin.coroutines.Continuation<? super java.io.File> $completion) {
        return null;
    }
    
    /**
     * 将PCM数据写入WAV文件
     * 添加正确的WAV文件头以确保ACRCloud能正确识别
     */
    private final void writeWavFile(java.io.File file, byte[] pcmData) {
    }
    
    /**
     * 将整数转换为小端字节数组
     */
    private final byte[] intToByteArray(int value) {
        return null;
    }
    
    /**
     * 将短整数转换为小端字节数组
     */
    private final byte[] shortToByteArray(int value) {
        return null;
    }
    
    /**
     * 解析识别响应
     */
    private final com.omar.musica.audiosearch.model.RecognizedSong parseRecognitionResponse(com.omar.musica.network.model.AudioRecognitionResponse response) {
        return null;
    }
    
    /**
     * 重置录制状态
     * 清除所有数据，回到初始状态
     */
    public final void resetRecording() {
    }
    
    /**
     * 重新识别
     */
    public final void retryRecognition() {
    }
    
    /**
     * 处理权限请求结果
     *
     * @param granted 用户是否授予了录音权限
     */
    public final void onPermissionResult(boolean granted) {
    }
    
    /**
     * 清除错误信息
     */
    public final void clearError() {
    }
    
    /**
     * 检查录音权限
     */
    private final void checkRecordPermission() {
    }
    
    /**
     * 检查API配置
     */
    private final void checkApiConfiguration() {
    }
    
    /**
     * 监听AudioRecorder的状态变化
     */
    private final void observeRecordingStatus() {
    }
    
    /**
     * 启动录制时长计时器
     * 每秒更新一次录制时长，用于UI显示
     */
    private final void startDurationTimer() {
    }
    
    /**
     * 停止录制时长计时器
     */
    private final void stopDurationTimer() {
    }
    
    /**
     * 重置识别状态
     */
    private final void resetRecognitionState() {
    }
    
    /**
     * 更新识别状态
     */
    private final void updateRecognitionStatus(com.omar.musica.audiosearch.ui.RecognitionStatus status) {
    }
    
    /**
     * 更新错误信息
     */
    private final void updateErrorMessage(java.lang.String message) {
    }
    
    @java.lang.Override
    protected void onCleared() {
    }
}