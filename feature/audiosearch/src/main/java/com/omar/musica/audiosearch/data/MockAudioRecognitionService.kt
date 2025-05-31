package com.omar.musica.audiosearch.data

import com.omar.musica.audiosearch.model.RecognizedSong
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 模拟音频识别服务
 * 用于开发阶段，无需真实API密钥
 * 模拟真实识别过程的延迟和结果
 */
@Singleton
class MockAudioRecognitionService @Inject constructor() {
    
    // 模拟的歌曲数据库
    private val mockSongs = listOf(
        RecognizedSong(
            title = "告白气球",
            artists = listOf("周杰伦"),
            album = "周杰伦的床边故事",
            durationMs = 203000L,
            genres = listOf("流行", "华语"),
            releaseDate = "2016-06-24",
            externalIds = RecognizedSong.ExternalIds(
                spotify = "4uLU6hMCjMI75M1A2tKUQC",
                youtube = "bu7uUBNmrA4",
                isrc = "TWA471600001"
            )
        ),
        RecognizedSong(
            title = "稻香",
            artists = listOf("周杰伦"),
            album = "魔杰座",
            durationMs = 220000L,
            genres = listOf("流行", "华语"),
            releaseDate = "2008-10-15",
            externalIds = RecognizedSong.ExternalIds(
                spotify = "1PBzs8HCyNqE2VaQUMhY0R",
                youtube = "MH4TLFbTyAY",
                isrc = "TWA470800009"
            )
        ),
        RecognizedSong(
            title = "青花瓷",
            artists = listOf("周杰伦"),
            album = "我很忙",
            durationMs = 230000L,
            genres = listOf("流行", "华语", "古风"),
            releaseDate = "2007-11-02",
            externalIds = RecognizedSong.ExternalIds(
                spotify = "6k8x8z23qkDTvl7rnLcVxL",
                youtube = "JBe8a8lHo-w",
                isrc = "TWA470700003"
            )
        ),
        RecognizedSong(
            title = "Shape of You",
            artists = listOf("Ed Sheeran"),
            album = "÷ (Divide)",
            durationMs = 233712L,
            genres = listOf("Pop", "Dance", "Tropical House"),
            releaseDate = "2017-01-06",
            externalIds = RecognizedSong.ExternalIds(
                spotify = "7qiZfU4dY1lWllzX7mPBI3",
                youtube = "JGwWNGJdvx8",
                isrc = "GBAHS1600214"
            )
        ),
        RecognizedSong(
            title = "晴天",
            artists = listOf("周杰伦"),
            album = "叶惠美",
            durationMs = 269000L,
            genres = listOf("流行", "华语"),
            releaseDate = "2003-07-31",
            externalIds = RecognizedSong.ExternalIds(
                spotify = "6DsFr3lHfGF0lRdBtB8txK",
                youtube = "lBhq6s8QU8A",
                isrc = "TWA470300011"
            )
        )
    )
    
    /**
     * 模拟音频识别过程
     * @param audioData 音频数据（在模拟模式下忽略）
     * @return 随机返回一首歌曲信息
     */
    suspend fun recognizeAudio(audioData: ByteArray?): RecognizedSong? {
        // 模拟网络请求延迟（2-5秒）
        val delay = (2000..5000).random()
        delay(delay.toLong())
        
        // 模拟识别成功率（80%成功率）
        val isSuccessful = (1..100).random() <= 80
        
        return if (isSuccessful) {
            // 随机返回一首歌
            mockSongs.random()
        } else {
            // 模拟识别失败
            null
        }
    }
    
    /**
     * 模拟获取识别进度
     */
    suspend fun getRecognitionProgress(): List<String> {
        val progressMessages = listOf(
            "正在分析音频特征...",
            "正在匹配音乐指纹...",
            "正在查询音乐数据库...",
            "正在获取歌曲信息...",
            "识别完成！"
        )
        
        return progressMessages
    }
} 