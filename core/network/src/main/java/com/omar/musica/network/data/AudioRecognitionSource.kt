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
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
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
            // 验证输入参数
            if (!audioFile.exists()) {
                throw NetworkErrorException("音频文件不存在")
            }
            
            if (audioFile.length() == 0L) {
                throw NetworkErrorException("音频文件为空")
            }
            
            if (accessKey.isEmpty() || accessSecret.isEmpty()) {
                throw NetworkErrorException("访问密钥或密码不能为空")
            }
            
            // 添加调试日志
            val fileSize = audioFile.length()
            val fileSizeKB = fileSize / 1024.0
            android.util.Log.d("AudioRecognition", "准备识别音频文件:")
            android.util.Log.d("AudioRecognition", "- 文件路径: ${audioFile.absolutePath}")
            android.util.Log.d("AudioRecognition", "- 文件大小: $fileSize bytes (${String.format("%.2f", fileSizeKB)} KB)")
            android.util.Log.d("AudioRecognition", "- 访问密钥: ${accessKey.take(4)}****${accessKey.takeLast(4)}")
            android.util.Log.d("AudioRecognition", "- 访问密码: ${if (accessSecret.isNotEmpty()) "${accessSecret.take(4)}****${accessSecret.takeLast(4)}" else "空"}")
            
            // 获取API Host信息（通过反射避免循环依赖）
            val apiHost = try {
                val configClass = Class.forName("com.omar.musica.audiosearch.config.AudioSearchConfig")
                val apiHostField = configClass.getDeclaredField("API_HOST")
                apiHostField.get(null) as String
            } catch (e: Exception) {
                "未知"
            }
            android.util.Log.d("AudioRecognition", "- API Host: $apiHost")
            
            // 验证密钥格式
            if (accessKey.length < 8 || accessSecret.length < 8) {
                android.util.Log.w("AudioRecognition", "警告：密钥长度可能不正确")
                android.util.Log.w("AudioRecognition", "- 访问密钥长度: ${accessKey.length}")
                android.util.Log.w("AudioRecognition", "- 访问密码长度: ${accessSecret.length}")
            }
            
            // 准备ACRCloud必需的参数
            val httpMethod = "POST"
            val httpUri = "/v1/identify"
            val dataType = "audio"
            val signatureVersion = "1"
            val timestamp = (System.currentTimeMillis() / 1000).toString()
            
            // 创建签名字符串
            val stringToSign = "$httpMethod\n$httpUri\n$accessKey\n$dataType\n$signatureVersion\n$timestamp"
            
            // 生成HMAC-SHA1签名
            val signature = generateSignature(stringToSign, accessSecret)
            
            android.util.Log.d("AudioRecognition", "签名参数:")
            android.util.Log.d("AudioRecognition", "- HTTP方法: $httpMethod")
            android.util.Log.d("AudioRecognition", "- URI: $httpUri") 
            android.util.Log.d("AudioRecognition", "- 数据类型: $dataType")
            android.util.Log.d("AudioRecognition", "- 签名版本: $signatureVersion")
            android.util.Log.d("AudioRecognition", "- 时间戳: $timestamp")
            android.util.Log.d("AudioRecognition", "- 文件大小: $fileSize")
            
            // 准备请求参数
            val accessKeyBody = accessKey.toRequestBody("text/plain".toMediaTypeOrNull())
            val accessSecretBody = accessSecret.toRequestBody("text/plain".toMediaTypeOrNull())
            val dataTypeBody = dataType.toRequestBody("text/plain".toMediaTypeOrNull())
            val signatureVersionBody = signatureVersion.toRequestBody("text/plain".toMediaTypeOrNull())
            val timestampBody = timestamp.toRequestBody("text/plain".toMediaTypeOrNull())
            val signatureBody = signature.toRequestBody("text/plain".toMediaTypeOrNull())
            val sampleBytesBody = fileSize.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            
            // 准备音频文件
            val audioRequestBody = audioFile.asRequestBody("audio/wav".toMediaTypeOrNull())
            val audioPart = MultipartBody.Part.createFormData("sample", audioFile.name, audioRequestBody)
            
            // 调用API
            val response = audioRecognitionService.recognizeAudio(
                accessKey = accessKeyBody,
                accessSecret = accessSecretBody,
                dataType = dataTypeBody,
                signatureVersion = signatureVersionBody,
                timestamp = timestampBody,
                signature = signatureBody,
                sampleBytes = sampleBytesBody,
                sample = audioPart
            )
            
            android.util.Log.d("AudioRecognition", "API响应状态码: ${response.status.code}")
            android.util.Log.d("AudioRecognition", "API响应消息: ${response.status.message}")
            
            // 检查响应状态
            when (response.status.code) {
                0 -> {
                    // 成功响应，打印详细信息
                    android.util.Log.d("AudioRecognition", "=== 识别成功，详细响应数据 ===")
                    android.util.Log.d("AudioRecognition", "响应版本: ${response.status.version}")
                    
                    // 打印 metadata 信息
                    response.metadata?.let { metadata ->
                        android.util.Log.d("AudioRecognition", "--- Metadata ---")
                        
                        // 打印音乐信息
                        metadata.music?.let { musicList ->
                            android.util.Log.d("AudioRecognition", "找到 ${musicList.size} 首歌曲:")
                            musicList.forEachIndexed { index, music ->
                                android.util.Log.d("AudioRecognition", "歌曲 ${index + 1}:")
                                android.util.Log.d("AudioRecognition", "  标题: ${music.title}")
                                android.util.Log.d("AudioRecognition", "  艺术家: ${music.artists.joinToString(", ") { it.name }}")
                                android.util.Log.d("AudioRecognition", "  专辑: ${music.album?.name ?: "未知"}")
                                android.util.Log.d("AudioRecognition", "  时长: ${music.durationMs}ms")
                                android.util.Log.d("AudioRecognition", "  发布日期: ${music.releaseDate ?: "未知"}")
                                android.util.Log.d("AudioRecognition", "  流派: ${music.genres?.joinToString(", ") { it.name } ?: "未知"}")
                                
                                // 外部ID
                                music.externalIds?.let { extIds ->
                                    android.util.Log.d("AudioRecognition", "  外部ID:")
                                    extIds.spotify?.let { android.util.Log.d("AudioRecognition", "    Spotify: $it") }
                                    extIds.youtube?.let { android.util.Log.d("AudioRecognition", "    YouTube: $it") }
                                    extIds.isrc?.let { android.util.Log.d("AudioRecognition", "    ISRC: $it") }
                                }
                            }
                        } ?: android.util.Log.d("AudioRecognition", "未找到音乐信息")
                        
                    } ?: android.util.Log.d("AudioRecognition", "响应中没有 metadata")
                    
                    android.util.Log.d("AudioRecognition", "=== 响应数据结束 ===")
                    
                    response // 返回成功响应
                }
                1001 -> throw NotFoundException("没有找到匹配的歌曲")
                2004 -> {
                    android.util.Log.w("AudioRecognition", "无法生成音频指纹 - 可能的原因:")
                    android.util.Log.w("AudioRecognition", "1. 录制的音频质量太差或太静")
                    android.util.Log.w("AudioRecognition", "2. 录制的不是音乐内容（纯噪音、静音等）")
                    android.util.Log.w("AudioRecognition", "3. 录制时长太短（建议10-15秒）")
                    android.util.Log.w("AudioRecognition", "4. 音频文件格式问题")
                    throw NetworkErrorException("无法识别音频内容 - 请确保：\n1. 在安静环境下录制清晰的音乐\n2. 录制时长至少10秒\n3. 音量适中，避免失真")
                }
                3000 -> throw NetworkErrorException("无效参数 - ${response.status.message}")
                3001 -> throw NetworkErrorException("API密钥无效")
                3003 -> throw NetworkErrorException("API配额已用完")
                else -> throw NetworkErrorException("识别失败: ${response.status.message} (错误码: ${response.status.code})")
            }
            
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            android.util.Log.e("AudioRecognition", "HTTP错误: ${e.code()}, 响应: $errorBody")
            when (e.code()) {
                404 -> throw NotFoundException("音频识别服务不可用")
                401 -> throw NetworkErrorException("API认证失败，请检查密钥配置")
                429 -> throw NetworkErrorException("请求频率过高，请稍后再试")
                400 -> throw NetworkErrorException("无效请求参数: $errorBody")
                else -> throw NetworkErrorException("网络请求失败: ${e.message()}")
            }
        } catch (e: NotFoundException) {
            throw e
        } catch (e: NetworkErrorException) {
            throw e
        } catch (e: Exception) {
            android.util.Log.e("AudioRecognition", "音频识别异常: ${e.message}", e)
            throw NetworkErrorException("音频识别失败: ${e.message ?: "未知错误"}")
        }
    }
    
    /**
     * 生成HMAC-SHA1签名
     */
    private fun generateSignature(stringToSign: String, accessSecret: String): String {
        return try {
            val secretKeySpec = SecretKeySpec(accessSecret.toByteArray(), "HmacSHA1")
            val mac = Mac.getInstance("HmacSHA1")
            mac.init(secretKeySpec)
            val rawHmac = mac.doFinal(stringToSign.toByteArray())
            Base64.getEncoder().encodeToString(rawHmac)
        } catch (e: NoSuchAlgorithmException) {
            throw NetworkErrorException("HMAC-SHA1算法不可用: ${e.message}")
        } catch (e: InvalidKeyException) {
            throw NetworkErrorException("无效的密钥: ${e.message}")
        }
    }
} 