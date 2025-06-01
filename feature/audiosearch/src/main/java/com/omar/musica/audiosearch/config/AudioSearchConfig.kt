package com.omar.musica.audiosearch.config

/**
 * 音频搜索配置类
 * 管理ACRCloud API相关的配置信息
 */
object AudioSearchConfig {
    
    /**
     * ACRCloud API密钥配置
     * TODO: 在生产环境中，这些密钥应该从安全的配置源获取，而不是硬编码
     */
    
    // 你的ACRCloud访问密钥
    const val ACCESS_KEY = "b3e294e77a4bee4b70ea2e578cbf0d6b"  // 请替换为你的实际密钥
    
    
    const val ACCESS_SECRET = "9bZJ1kubXbSNkNGEYiTnJ1sJbtkqKPmWAkJ01OMG"  // 根据你的ACRCloud配置调整
    
    /**
     * ACRCloud API Host配置
     * 不同账户和地区可能有不同的host
     * 
     * 可能的hosts：
     * - https://identify-us-west-2.acrcloud.com/ (默认美西)
     * - https://identify-eu-west-1.acrcloud.com/ (欧洲)
     * - https://identify-ap-southeast-1.acrcloud.com/ (亚太)
     * - 或你账户专用的自定义host
     * 
     * 如果你是老版本账户，可能需要尝试以下hosts：
     * - https://identify-us-west-2.acrcloud.com/
     * - https://identify-eu-west-1.acrcloud.com/
     * - https://identify-ap-southeast-1.acrcloud.com/
     * - https://identify-cn-north-1.acrcloud.cn/
     * 
     * 注意：如果遇到"API密钥无效"错误，请尝试更换不同的host
     */
    const val API_HOST = "https://identify-cn-north-1.acrcloud.cn/"
    
    /**
     * 备用API Hosts（用于故障排除）
     */
    val ALTERNATIVE_HOSTS = listOf(
        "https://identify-us-west-2.acrcloud.com/",
        "https://identify-eu-west-1.acrcloud.com/",
        "https://identify-ap-southeast-1.acrcloud.com/",
        "https://identify-cn-north-1.acrcloud.cn/"
    )
    
    /**
     * API配置
     */
    const val REQUEST_TIMEOUT_SECONDS = 30
    const val MAX_AUDIO_FILE_SIZE_MB = 1  // 1MB，ACRCloud建议小于1MB
    const val RECOMMENDED_AUDIO_DURATION_SECONDS = 15  // 推荐音频时长
    
    /**
     * 识别配置
     */
    const val MIN_RECORDING_DURATION_SECONDS = 3  // 最短录制时长
    const val MAX_RECORDING_DURATION_SECONDS = 30  // 最长录制时长
    
    /**
     * 检查API密钥是否已配置
     */
    fun isApiKeyConfigured(): Boolean {
        return ACCESS_KEY.isNotEmpty() && ACCESS_KEY != "YOUR_ACRCLOUD_KEY_HERE"
    }
    
    /**
     * 获取用于显示的API密钥（隐藏敏感信息）
     */
    fun getMaskedApiKey(): String {
        return if (ACCESS_KEY.length > 8) {
            "${ACCESS_KEY.take(4)}****${ACCESS_KEY.takeLast(4)}"
        } else {
            "****"
        }
    }
    
    /**
     * 获取完整的API URL
     */
    fun getApiUrl(): String {
        return "${API_HOST}v1/identify"
    }
    
    /**
     * 获取API Host的域名部分（用于调试）
     */
    fun getHostname(): String {
        return try {
            val url = java.net.URL(API_HOST)
            url.host
        } catch (e: Exception) {
            "unknown"
        }
    }
    
    /**
     * 获取用于诊断的所有可能hosts
     */
    fun getAllPossibleHosts(): List<String> {
        return ALTERNATIVE_HOSTS
    }
    
    /**
     * 获取账户配置诊断信息
     */
    fun getDiagnosticInfo(): String {
        return """
            ACRCloud账户配置诊断：
            - 访问密钥: ${getMaskedApiKey()}
            - 密钥长度: ${ACCESS_KEY.length} 字符
            - 密码长度: ${ACCESS_SECRET.length} 字符
            - 当前Host: $API_HOST
            - Host域名: ${getHostname()}
            
            故障排除建议：
            1. 如果遇到"API密钥无效"，请尝试不同的host
            2. 确保密钥来自正确的ACRCloud项目
            3. 检查项目是否启用了音频识别服务
            4. 老版本账户可能需要特定的host配置
        """.trimIndent()
    }
} 