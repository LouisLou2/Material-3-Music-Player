package com.omar.musica.network.data

import com.omar.musica.network.model.AudioRecognitionResponse
import com.omar.musica.network.model.NetworkErrorException
import com.omar.musica.network.model.NotFoundException
import com.omar.musica.network.service.AudioRecognitionService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 音频识别数据源
 * 处理音频识别相关的网络请求和错误处理
 */
@Singleton
class AudioRecognitionSource @Inject constructor(
    private val audioRecognitionService: AudioRecognitionService
) {

    /**
     * 识别音频文件中的歌曲
     * @param audioFile 音频文件
     * @param accessKey API访问密钥
     * @param accessSecret API访问密码
     * @return 识别结果
     * @throws NotFoundException 当没有找到匹配的歌曲时
     * @throws NetworkErrorException 当网络请求失败时
     */
    suspend fun recognizeAudio(
        audioFile: File,
        accessKey: String,
        accessSecret: String
    ): AudioRecognitionResponse {
        return try {
            // 准备请求参数
            val accessKeyBody = accessKey.toRequestBody("text/plain".toMediaTypeOrNull())
            val accessSecretBody = accessSecret.toRequestBody("text/plain".toMediaTypeOrNull())
            val dataTypeBody = "audio".toRequestBody("text/plain".toMediaTypeOrNull())
            val sampleRateBody = "44100".toRequestBody("text/plain".toMediaTypeOrNull())
            val timestampBody = (System.currentTimeMillis() / 1000).toString()
                .toRequestBody("text/plain".toMediaTypeOrNull())
            
            // 准备音频文件
            val audioRequestBody = audioFile.asRequestBody("audio/wav".toMediaTypeOrNull())
            val audioPart = MultipartBody.Part.createFormData("sample", audioFile.name, audioRequestBody)
            
            // 发送识别请求
            val response = audioRecognitionService.recognizeAudio(
                accessKey = accessKeyBody,
                accessSecret = accessSecretBody,
                dataType = dataTypeBody,
                sample = audioPart,
                sampleRate = sampleRateBody,
                timestamp = timestampBody
            )
            
            // 检查响应状态
            when (response.status.code) {
                0 -> response // 成功
                1001 -> throw NotFoundException("没有找到匹配的歌曲")
                3001 -> throw NetworkErrorException("API密钥无效")
                3003 -> throw NetworkErrorException("API配额已用完")
                else -> throw NetworkErrorException("识别失败: ${response.status.message}")
            }
            
        } catch (e: HttpException) {
            when (e.code()) {
                404 -> throw NotFoundException("音频识别服务不可用")
                401 -> throw NetworkErrorException("API认证失败")
                429 -> throw NetworkErrorException("请求频率过高，请稍后再试")
                else -> throw NetworkErrorException("网络请求失败: ${e.message()}")
            }
        } catch (e: NotFoundException) {
            throw e
        } catch (e: NetworkErrorException) {
            throw e
        } catch (e: Exception) {
            throw NetworkErrorException("音频识别失败: ${e.message ?: "未知错误"}")
        }
    }
} 