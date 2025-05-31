package com.omar.musica.audiosearch.data.recorder;

/**
 * 录音状态枚举
 * 用于管理录音过程中的不同阶段，便于UI显示和状态控制
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0006\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/omar/musica/audiosearch/data/recorder/RecordingStatus;", "", "(Ljava/lang/String;I)V", "IDLE", "RECORDING", "COMPLETED", "ERROR", "audiosearch_debug"})
public enum RecordingStatus {
    /*public static final*/ IDLE /* = new IDLE() */,
    /*public static final*/ RECORDING /* = new RECORDING() */,
    /*public static final*/ COMPLETED /* = new COMPLETED() */,
    /*public static final*/ ERROR /* = new ERROR() */;
    
    RecordingStatus() {
    }
    
    @org.jetbrains.annotations.NotNull
    public static kotlin.enums.EnumEntries<com.omar.musica.audiosearch.data.recorder.RecordingStatus> getEntries() {
        return null;
    }
}