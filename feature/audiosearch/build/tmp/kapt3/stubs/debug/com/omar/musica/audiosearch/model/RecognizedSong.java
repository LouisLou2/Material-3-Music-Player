package com.omar.musica.audiosearch.model;

/**
 * 识别到的歌曲信息
 * 用于在UI中显示音频识别结果
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0015\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0003\b\u0086\b\u0018\u00002\u00020\u0001:\u0001*BQ\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00030\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u000e\u0010\t\u001a\n\u0012\u0004\u0012\u00020\u0003\u0018\u00010\u0005\u0012\b\u0010\n\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u000b\u001a\u0004\u0018\u00010\f\u00a2\u0006\u0002\u0010\rJ\t\u0010\u0019\u001a\u00020\u0003H\u00c6\u0003J\u000f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00030\u0005H\u00c6\u0003J\u000b\u0010\u001b\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u0010\u001c\u001a\u00020\bH\u00c6\u0003J\u0011\u0010\u001d\u001a\n\u0012\u0004\u0012\u00020\u0003\u0018\u00010\u0005H\u00c6\u0003J\u000b\u0010\u001e\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010\u001f\u001a\u0004\u0018\u00010\fH\u00c6\u0003Jc\u0010 \u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00030\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0007\u001a\u00020\b2\u0010\b\u0002\u0010\t\u001a\n\u0012\u0004\u0012\u00020\u0003\u0018\u00010\u00052\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00c6\u0001J\u0013\u0010!\u001a\u00020\"2\b\u0010#\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\u0006\u0010$\u001a\u00020\u0003J\u0006\u0010%\u001a\u00020\u0003J\b\u0010&\u001a\u0004\u0018\u00010\u0003J\t\u0010\'\u001a\u00020(H\u00d6\u0001J\t\u0010)\u001a\u00020\u0003H\u00d6\u0001R\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0017\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00030\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0013\u0010\u000b\u001a\u0004\u0018\u00010\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0019\u0010\t\u001a\n\u0012\u0004\u0012\u00020\u0003\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0011R\u0013\u0010\n\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u000fR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u000f\u00a8\u0006+"}, d2 = {"Lcom/omar/musica/audiosearch/model/RecognizedSong;", "", "title", "", "artists", "", "album", "durationMs", "", "genres", "releaseDate", "externalIds", "Lcom/omar/musica/audiosearch/model/RecognizedSong$ExternalIds;", "(Ljava/lang/String;Ljava/util/List;Ljava/lang/String;JLjava/util/List;Ljava/lang/String;Lcom/omar/musica/audiosearch/model/RecognizedSong$ExternalIds;)V", "getAlbum", "()Ljava/lang/String;", "getArtists", "()Ljava/util/List;", "getDurationMs", "()J", "getExternalIds", "()Lcom/omar/musica/audiosearch/model/RecognizedSong$ExternalIds;", "getGenres", "getReleaseDate", "getTitle", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "", "other", "getArtistsString", "getFormattedDuration", "getGenresString", "hashCode", "", "toString", "ExternalIds", "audiosearch_debug"})
public final class RecognizedSong {
    @org.jetbrains.annotations.NotNull
    private final java.lang.String title = null;
    @org.jetbrains.annotations.NotNull
    private final java.util.List<java.lang.String> artists = null;
    @org.jetbrains.annotations.Nullable
    private final java.lang.String album = null;
    private final long durationMs = 0L;
    @org.jetbrains.annotations.Nullable
    private final java.util.List<java.lang.String> genres = null;
    @org.jetbrains.annotations.Nullable
    private final java.lang.String releaseDate = null;
    @org.jetbrains.annotations.Nullable
    private final com.omar.musica.audiosearch.model.RecognizedSong.ExternalIds externalIds = null;
    
    public RecognizedSong(@org.jetbrains.annotations.NotNull
    java.lang.String title, @org.jetbrains.annotations.NotNull
    java.util.List<java.lang.String> artists, @org.jetbrains.annotations.Nullable
    java.lang.String album, long durationMs, @org.jetbrains.annotations.Nullable
    java.util.List<java.lang.String> genres, @org.jetbrains.annotations.Nullable
    java.lang.String releaseDate, @org.jetbrains.annotations.Nullable
    com.omar.musica.audiosearch.model.RecognizedSong.ExternalIds externalIds) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getTitle() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<java.lang.String> getArtists() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getAlbum() {
        return null;
    }
    
    public final long getDurationMs() {
        return 0L;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.util.List<java.lang.String> getGenres() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getReleaseDate() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.omar.musica.audiosearch.model.RecognizedSong.ExternalIds getExternalIds() {
        return null;
    }
    
    /**
     * 获取格式化的艺术家字符串
     */
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getArtistsString() {
        return null;
    }
    
    /**
     * 获取格式化的时长字符串
     */
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getFormattedDuration() {
        return null;
    }
    
    /**
     * 获取格式化的流派字符串
     */
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getGenresString() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<java.lang.String> component2() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component3() {
        return null;
    }
    
    public final long component4() {
        return 0L;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.util.List<java.lang.String> component5() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.omar.musica.audiosearch.model.RecognizedSong.ExternalIds component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.omar.musica.audiosearch.model.RecognizedSong copy(@org.jetbrains.annotations.NotNull
    java.lang.String title, @org.jetbrains.annotations.NotNull
    java.util.List<java.lang.String> artists, @org.jetbrains.annotations.Nullable
    java.lang.String album, long durationMs, @org.jetbrains.annotations.Nullable
    java.util.List<java.lang.String> genres, @org.jetbrains.annotations.Nullable
    java.lang.String releaseDate, @org.jetbrains.annotations.Nullable
    com.omar.musica.audiosearch.model.RecognizedSong.ExternalIds externalIds) {
        return null;
    }
    
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public java.lang.String toString() {
        return null;
    }
    
    /**
     * 外部平台ID
     */
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B)\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\u0006J\u000b\u0010\u000b\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010\f\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010\r\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J-\u0010\u000e\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003H\u00c6\u0001J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0012\u001a\u00020\u0013H\u00d6\u0001J\t\u0010\u0014\u001a\u00020\u0003H\u00d6\u0001R\u0013\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\bR\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\b\u00a8\u0006\u0015"}, d2 = {"Lcom/omar/musica/audiosearch/model/RecognizedSong$ExternalIds;", "", "spotify", "", "youtube", "isrc", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getIsrc", "()Ljava/lang/String;", "getSpotify", "getYoutube", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "", "toString", "audiosearch_debug"})
    public static final class ExternalIds {
        @org.jetbrains.annotations.Nullable
        private final java.lang.String spotify = null;
        @org.jetbrains.annotations.Nullable
        private final java.lang.String youtube = null;
        @org.jetbrains.annotations.Nullable
        private final java.lang.String isrc = null;
        
        public ExternalIds(@org.jetbrains.annotations.Nullable
        java.lang.String spotify, @org.jetbrains.annotations.Nullable
        java.lang.String youtube, @org.jetbrains.annotations.Nullable
        java.lang.String isrc) {
            super();
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.lang.String getSpotify() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.lang.String getYoutube() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.lang.String getIsrc() {
            return null;
        }
        
        public ExternalIds() {
            super();
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.lang.String component1() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.lang.String component2() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.lang.String component3() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.omar.musica.audiosearch.model.RecognizedSong.ExternalIds copy(@org.jetbrains.annotations.Nullable
        java.lang.String spotify, @org.jetbrains.annotations.Nullable
        java.lang.String youtube, @org.jetbrains.annotations.Nullable
        java.lang.String isrc) {
            return null;
        }
        
        @java.lang.Override
        public boolean equals(@org.jetbrains.annotations.Nullable
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override
        @org.jetbrains.annotations.NotNull
        public java.lang.String toString() {
            return null;
        }
    }
}