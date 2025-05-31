package com.omar.musica.network.service

import com.omar.musica.network.model.AudioRecognitionResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

/**
 * 音频识别服务接口
 * 用于与音频识别API进行通信
 */
interface AudioRecognitionService {

    companion object {
        // 使用ACRCloud作为音频识别服务
        const val BASE_URL = "https://identify-us-west-2.acrcloud.com/"
    }

    /**
     * 发送音频数据进行识别
     * @param accessKey API访问密钥
     * @param accessSecret API访问密码
     * @param dataType 数据类型，固定为"audio"
     * @param sampleBytes 音频数据文件
     * @param sampleRate 采样率
     * @param timestamp 时间戳
     * @return 识别结果
     */
    @Multipart
    @POST("v1/identify")
    suspend fun recognizeAudio(
        @Part("access_key") accessKey: okhttp3.RequestBody,
        @Part("access_secret") accessSecret: okhttp3.RequestBody,
        @Part("data_type") dataType: okhttp3.RequestBody,
        @Part sample: MultipartBody.Part,
        @Part("sample_rate") sampleRate: okhttp3.RequestBody,
        @Part("timestamp") timestamp: okhttp3.RequestBody
    ): AudioRecognitionResponse
} 