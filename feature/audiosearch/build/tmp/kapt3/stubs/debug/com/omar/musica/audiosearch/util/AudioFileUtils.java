package com.omar.musica.audiosearch.util;

/**
 * 音频文件工具类
 * 用于保存和管理录制的音频文件
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u000bJ\u000e\u0010\f\u001a\u00020\u00042\u0006\u0010\r\u001a\u00020\u000eJ\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00110\u00102\u0006\u0010\b\u001a\u00020\tJ\"\u0010\u0012\u001a\u0004\u0018\u00010\u00112\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010\u0015\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2 = {"Lcom/omar/musica/audiosearch/util/AudioFileUtils;", "", "()V", "AUDIO_DIR", "", "AUDIO_FILE_EXTENSION", "cleanupOldAudioFiles", "", "context", "Landroid/content/Context;", "maxFiles", "", "formatFileSize", "sizeInBytes", "", "getAudioFiles", "", "Ljava/io/File;", "saveAudioToFile", "audioData", "", "fileName", "audiosearch_debug"})
public final class AudioFileUtils {
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String AUDIO_DIR = "recorded_audio";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String AUDIO_FILE_EXTENSION = ".pcm";
    @org.jetbrains.annotations.NotNull
    public static final com.omar.musica.audiosearch.util.AudioFileUtils INSTANCE = null;
    
    private AudioFileUtils() {
        super();
    }
    
    /**
     * 保存音频数据到文件
     * @param context 上下文
     * @param audioData 音频数据
     * @param fileName 文件名（不包含扩展名）
     * @return 保存的文件，如果失败则返回null
     */
    @org.jetbrains.annotations.Nullable
    public final java.io.File saveAudioToFile(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    byte[] audioData, @org.jetbrains.annotations.NotNull
    java.lang.String fileName) {
        return null;
    }
    
    /**
     * 删除旧的音频文件
     * @param context 上下文
     * @param maxFiles 保留的最大文件数
     */
    public final void cleanupOldAudioFiles(@org.jetbrains.annotations.NotNull
    android.content.Context context, int maxFiles) {
    }
    
    /**
     * 获取音频文件列表
     * @param context 上下文
     * @return 音频文件列表
     */
    @org.jetbrains.annotations.NotNull
    public final java.util.List<java.io.File> getAudioFiles(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
        return null;
    }
    
    /**
     * 格式化文件大小
     * @param sizeInBytes 文件大小（字节）
     * @return 格式化的文件大小字符串
     */
    @org.jetbrains.annotations.NotNull
    public final java.lang.String formatFileSize(long sizeInBytes) {
        return null;
    }
}