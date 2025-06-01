package com.omar.musica.audiosearch.config;

/**
 * 音频搜索配置类
 * 管理ACRCloud API相关的配置信息
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\n\n\u0002\u0010\u000b\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00040\u0007J\u0006\u0010\u0012\u001a\u00020\u0004J\u0006\u0010\u0013\u001a\u00020\u0004J\u0006\u0010\u0014\u001a\u00020\u0004J\u0006\u0010\u0015\u001a\u00020\u0004J\u0006\u0010\u0016\u001a\u00020\u0017R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00040\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u000e\u0010\n\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\fX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\fX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\fX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\fX\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2 = {"Lcom/omar/musica/audiosearch/config/AudioSearchConfig;", "", "()V", "ACCESS_KEY", "", "ACCESS_SECRET", "ALTERNATIVE_HOSTS", "", "getALTERNATIVE_HOSTS", "()Ljava/util/List;", "API_HOST", "MAX_AUDIO_FILE_SIZE_MB", "", "MAX_RECORDING_DURATION_SECONDS", "MIN_RECORDING_DURATION_SECONDS", "RECOMMENDED_AUDIO_DURATION_SECONDS", "REQUEST_TIMEOUT_SECONDS", "getAllPossibleHosts", "getApiUrl", "getDiagnosticInfo", "getHostname", "getMaskedApiKey", "isApiKeyConfigured", "", "audiosearch_debug"})
public final class AudioSearchConfig {
    
    /**
     * ACRCloud API密钥配置
     * TODO: 在生产环境中，这些密钥应该从安全的配置源获取，而不是硬编码
     */
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String ACCESS_KEY = "b3e294e77a4bee4b70ea2e578cbf0d6b";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String ACCESS_SECRET = "9bZJ1kubXbSNkNGEYiTnJ1sJbtkqKPmWAkJ01OMG";
    
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
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String API_HOST = "https://identify-cn-north-1.acrcloud.cn/";
    
    /**
     * 备用API Hosts（用于故障排除）
     */
    @org.jetbrains.annotations.NotNull
    private static final java.util.List<java.lang.String> ALTERNATIVE_HOSTS = null;
    
    /**
     * API配置
     */
    public static final int REQUEST_TIMEOUT_SECONDS = 30;
    public static final int MAX_AUDIO_FILE_SIZE_MB = 1;
    public static final int RECOMMENDED_AUDIO_DURATION_SECONDS = 15;
    
    /**
     * 识别配置
     */
    public static final int MIN_RECORDING_DURATION_SECONDS = 3;
    public static final int MAX_RECORDING_DURATION_SECONDS = 30;
    @org.jetbrains.annotations.NotNull
    public static final com.omar.musica.audiosearch.config.AudioSearchConfig INSTANCE = null;
    
    private AudioSearchConfig() {
        super();
    }
    
    /**
     * 备用API Hosts（用于故障排除）
     */
    @org.jetbrains.annotations.NotNull
    public final java.util.List<java.lang.String> getALTERNATIVE_HOSTS() {
        return null;
    }
    
    /**
     * 检查API密钥是否已配置
     */
    public final boolean isApiKeyConfigured() {
        return false;
    }
    
    /**
     * 获取用于显示的API密钥（隐藏敏感信息）
     */
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getMaskedApiKey() {
        return null;
    }
    
    /**
     * 获取完整的API URL
     */
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getApiUrl() {
        return null;
    }
    
    /**
     * 获取API Host的域名部分（用于调试）
     */
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getHostname() {
        return null;
    }
    
    /**
     * 获取用于诊断的所有可能hosts
     */
    @org.jetbrains.annotations.NotNull
    public final java.util.List<java.lang.String> getAllPossibleHosts() {
        return null;
    }
    
    /**
     * 获取账户配置诊断信息
     */
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getDiagnosticInfo() {
        return null;
    }
}