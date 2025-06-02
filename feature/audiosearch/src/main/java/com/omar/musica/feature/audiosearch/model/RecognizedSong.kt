package com.omar.musica.audiosearch.model

/**
 * 识别到的歌曲信息
 * 用于在UI中显示音频识别结果
 */
data class RecognizedSong(
    val title: String,
    val artists: List<String>,
    val album: String?,
    val durationMs: Long,
    val genres: List<String>?,
    val releaseDate: String?,
    val externalIds: ExternalIds?
) {
    
    /**
     * 外部平台ID
     */
    data class ExternalIds(
        val spotify: String? = null,
        val youtube: String? = null,
        val isrc: String? = null
    )
    
    /**
     * 获取格式化的艺术家字符串
     */
    fun getArtistsString(): String = artists.joinToString(", ")
    
    /**
     * 获取格式化的时长字符串
     */
    fun getFormattedDuration(): String {
        val seconds = (durationMs / 1000) % 60
        val minutes = (durationMs / (1000 * 60)) % 60
        val hours = (durationMs / (1000 * 60 * 60))
        
        return if (hours > 0) {
            String.format("%d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format("%d:%02d", minutes, seconds)
        }
    }
    
    /**
     * 获取格式化的流派字符串
     */
    fun getGenresString(): String? = genres?.joinToString(", ")
} 