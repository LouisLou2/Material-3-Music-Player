package com.omar.musica.audiosearch.data.recorder

/**
 * 录音状态枚举
 * 用于管理录音过程中的不同阶段，便于UI显示和状态控制
 */
enum class RecordingStatus {
    /** 空闲状态 - 未开始录音 */
    IDLE,
    
    /** 录音中 - 正在录制音频 */
    RECORDING,
    
    /** 录音完成 - 已停止录制，音频数据已保存 */
    COMPLETED,
    
    /** 录音失败 - 由于权限或硬件问题导致录制失败 */
    ERROR
} 