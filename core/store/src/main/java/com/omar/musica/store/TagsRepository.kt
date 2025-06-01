package com.omar.musica.store

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import com.omar.musica.model.song.BasicSongMetadata
import com.omar.musica.model.song.ExtendedSongMetadata
import com.omar.musica.store.model.tags.SongTags
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import org.jaudiotagger.tag.TagOptionSingleton
import org.jaudiotagger.tag.images.AndroidArtwork
import java.io.File
import java.io.FileNotFoundException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random
import timber.log.Timber


@Singleton
class TagsRepository @Inject constructor(
    @ApplicationContext val context: Context,
    val mediaRepository: MediaRepository,
) {


    suspend fun getSongTags(songUri: Uri): SongTags = withContext(Dispatchers.IO) {

        val metadataRetriever = MediaMetadataRetriever().apply { setDataSource(context, songUri) }

        val title =
            metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE) ?: ""
        val artist =
            metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST) ?: ""
        val album =
            metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM) ?: ""
        val albumArtist =
            metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST) ?: ""
        val composer =
            metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_COMPOSER) ?: ""
        val genre =
            metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE) ?: ""
        val year = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR) ?: ""
        val trackNumber =
            metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER)
                ?: ""
        val discNumber =
            metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DISC_NUMBER) ?: ""

        val artwork: Bitmap? = metadataRetriever.embeddedPicture?.let {
            return@let BitmapFactory.decodeByteArray(it, 0, it.size)
        }

        SongTags(
            songUri,
            artwork,
            ExtendedSongMetadata(
                BasicSongMetadata(title, artist, album, 0, 0),
                albumArtist, composer, trackNumber, discNumber, genre, year, ""
            )
        )
    }

    /**
     * Edits the tag of some song to [songTags].
     * This doesn't check for permission. Instead, permission should be checked in the UI
     * before calling this method
     */
    suspend fun editTags(uri: Uri, songTags: SongTags) = withContext(Dispatchers.IO) {
        TagOptionSingleton.getInstance().isAndroid = true;

        val basicMetadata = songTags.metadata.basicSongMetadata
        val artwork = songTags.artwork
        val extendedMetadata = songTags.metadata

        val filePath = mediaRepository.getSongPath(uri)
        val originalSongFile = File(filePath)
        
        Timber.d("编辑标签的文件路径: $filePath")
        
        // 检查文件是否存在和可读
        if (!originalSongFile.exists()) {
            Timber.e("文件不存在: $filePath")
            throw FileNotFoundException("音频文件不存在: $filePath")
        }
        
        if (!originalSongFile.canRead()) {
            Timber.e("文件不可读: $filePath")
            throw SecurityException("无法读取音频文件: $filePath")
        }
        
        if (!originalSongFile.canWrite()) {
            Timber.w("文件不可写: $filePath")
        }

        val bitmapTempFile =
            if (artwork != null)
                context.saveTempBitmapForArtwork(artwork)
            else null

        val songAndroidArtwork = if (artwork != null)
            AndroidArtwork.createArtworkFromFile(bitmapTempFile)
        else
            null

        try {
            val audioFileIO = AudioFileIO.read(originalSongFile)
            val newTag = audioFileIO.tagOrCreateAndSetDefault.run {
                // 安全设置字段，避免null值
                Timber.d("设置标签字段开始")
                
                try {
                    if (basicMetadata.title.isNotEmpty()) {
                        Timber.d("设置标题: ${basicMetadata.title}")
                        setField(FieldKey.TITLE, basicMetadata.title)
                    }
                } catch (e: Exception) {
                    Timber.w(e, "设置标题字段失败")
                }
                
                try {
                    if (!basicMetadata.artistName.isNullOrEmpty()) {
                        Timber.d("设置艺术家: ${basicMetadata.artistName}")
                        setField(FieldKey.ARTIST, basicMetadata.artistName)
                    }
                } catch (e: Exception) {
                    Timber.w(e, "设置艺术家字段失败")
                }
                
                try {
                    if (!basicMetadata.albumName.isNullOrEmpty()) {
                        Timber.d("设置专辑: ${basicMetadata.albumName}")
                        setField(FieldKey.ALBUM, basicMetadata.albumName)
                    }
                } catch (e: Exception) {
                    Timber.w(e, "设置专辑字段失败")
                }
                
                try {
                    if (extendedMetadata.albumArtist.isNotEmpty()) {
                        Timber.d("设置专辑艺术家: ${extendedMetadata.albumArtist}")
                        setField(FieldKey.ALBUM_ARTIST, extendedMetadata.albumArtist)
                    }
                } catch (e: Exception) {
                    Timber.w(e, "设置专辑艺术家字段失败")
                }
                
                try {
                    if (extendedMetadata.genre.isNotEmpty()) {
                        Timber.d("设置类型: ${extendedMetadata.genre}")
                        setField(FieldKey.GENRE, extendedMetadata.genre)
                    }
                } catch (e: Exception) {
                    Timber.w(e, "设置类型字段失败")
                }
                
                try {
                    if (extendedMetadata.year.isNotEmpty()) {
                        Timber.d("设置年份: ${extendedMetadata.year}")
                        setField(FieldKey.YEAR, extendedMetadata.year)
                    }
                } catch (e: Exception) {
                    Timber.w(e, "设置年份字段失败")
                }
                
                try {
                    if (extendedMetadata.composer.isNotEmpty()) {
                        Timber.d("设置作曲者: ${extendedMetadata.composer}")
                        setField(FieldKey.COMPOSER, extendedMetadata.composer)
                    }
                } catch (e: Exception) {
                    Timber.w(e, "设置作曲者字段失败")
                }
                
                try {
                    if (extendedMetadata.trackNumber.isNotEmpty()) {
                        Timber.d("设置曲目号: ${extendedMetadata.trackNumber}")
                        setField(FieldKey.TRACK, extendedMetadata.trackNumber)
                    }
                } catch (e: Exception) {
                    Timber.w(e, "设置曲目号字段失败")
                }
                
                try {
                    if (extendedMetadata.discNumber.isNotEmpty()) {
                        Timber.d("设置碟片号: ${extendedMetadata.discNumber}")
                        setField(FieldKey.DISC_NO, extendedMetadata.discNumber)
                    }
                } catch (e: Exception) {
                    Timber.w(e, "设置碟片号字段失败")
                }
                
                // 处理封面图片
                try {
                    Timber.d("删除现有封面")
                    deleteArtworkField()
                } catch (e: Exception) {
                    Timber.w(e, "删除封面字段失败")
                }
                
                try {
                    if (songAndroidArtwork != null) {
                        Timber.d("设置新封面")
                        setField(songAndroidArtwork)
                    }
                } catch (e: Exception) {
                    Timber.w(e, "设置封面字段失败")
                }
                
                Timber.d("标签字段设置完成")
                this
            }
            audioFileIO.tag = newTag


            // need to save song in app-specific directory and then copy it to original dir
            // because the tagging library has to create temp files and this is forbidden in android R :(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val cacheFile = File(context.cacheDir, originalSongFile.name)
                cacheFile.outputStream().use { cache ->
                    originalSongFile.inputStream().use {
                        it.copyTo(cache)
                    }
                }
                audioFileIO.file = cacheFile
                audioFileIO.commit()
                // copy back to the original directory ( I hate android R)
                // In Android Q, for some unknown reason, using the File Api directly results in permission
                // denied, even though we have requestLegacyExternalStorage enabled
                // but opening it with contentResolver works for some reason ¯\_(ツ)_/¯
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
                    val os = context.contentResolver.openOutputStream(uri)!!
                    os.use { stream ->
                        cacheFile.inputStream().use {
                            it.copyTo(stream)
                        }
                    }
                } else {
                    originalSongFile.outputStream().use { original ->
                        cacheFile.inputStream().use {
                            it.copyTo(original)
                        }
                    }
                }
                cacheFile.delete()
            } else {
                audioFileIO.commit()
            }
            
            Timber.d("标签编辑完成，开始媒体扫描")
            MediaScannerConnection.scanFile(context, arrayOf(filePath), null, null)
            
        } catch (e: Exception) {
            Timber.e(e, "AudioFileIO 处理失败")
            when {
                e::class.java.simpleName.contains("CannotReadException") -> {
                    throw IllegalArgumentException("不支持的音频文件格式或文件已损坏", e)
                }
                e::class.java.simpleName.contains("CannotWriteException") -> {
                    throw SecurityException("无法写入音频文件", e)
                }
                else -> throw e
            }
        } finally {
            bitmapTempFile?.delete()
        }
    }


    private suspend fun Context.saveTempBitmapForArtwork(bitmap: Bitmap): File =
        withContext(Dispatchers.IO) {
            val tempFile = File(cacheDir, "album_art_temp_${Random.nextLong()}")
            tempFile.outputStream().use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            }
            tempFile
        }

}