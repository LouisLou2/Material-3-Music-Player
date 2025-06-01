package com.omar.musica.network.service

import com.omar.musica.network.model.AudioRecognitionResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

/**
 * 音频识别服务接口
 * 用于与ACRCloud音频识别API进行通信
 * 
 * 根据ACRCloud官方文档实现，需要以下必需参数：
 * - access_key: 项目访问密钥
 * - access_secret: 项目访问密码
 * - data_type: 数据类型（"audio"或"fingerprint"）
 * - signature_version: 签名版本（固定为"1"）
 * - timestamp: 时间戳
 * - signature: HMAC-SHA1签名
 * - sample_bytes: 文件大小
 * - sample: 音频文件
 * 
 * 注意：不同的ACRCloud账户可能有不同的API Host
 */
interface AudioRecognitionService {

    /**
     * 发送音频数据进行识别
     * 
     * ACRCloud API使用multipart/form-data格式提交请求
     * 必须包含HMAC-SHA1签名以进行身份验证
     * 
     * @param accessKey API访问密钥
     * @param accessSecret API访问密码
     * @param dataType 数据类型，固定为"audio"
     * @param signatureVersion 签名版本，固定为"1"
     * @param timestamp 时间戳（自Unix纪元以来的秒数）
     * @param signature HMAC-SHA1签名
     * @param sampleBytes 音频文件大小（字节）
     * @param sample 音频数据文件
     * @return 识别结果
     */
    @Multipart
    @POST("v1/identify")
    suspend fun recognizeAudio(
        @Part("access_key") accessKey: okhttp3.RequestBody,
        @Part("access_secret") accessSecret: okhttp3.RequestBody,
        @Part("data_type") dataType: okhttp3.RequestBody,
        @Part("signature_version") signatureVersion: okhttp3.RequestBody,
        @Part("timestamp") timestamp: okhttp3.RequestBody,
        @Part("signature") signature: okhttp3.RequestBody,
        @Part("sample_bytes") sampleBytes: okhttp3.RequestBody,
        @Part sample: MultipartBody.Part
    ): AudioRecognitionResponse
} 