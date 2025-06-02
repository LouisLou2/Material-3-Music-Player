package com.omar.musica.folders.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.musica.model.FoldersSortOption
import com.omar.musica.model.prefs.IsAscending
import com.omar.musica.playback.PlaybackManager
import com.omar.musica.store.FoldersRepository
import com.omar.musica.store.MediaRepository
import com.omar.musica.store.model.album.BasicAlbum
import com.omar.musica.store.model.song.Song
import com.omar.musica.store.preferences.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class FoldersViewModel @Inject constructor(
    private val foldersRepository: FoldersRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val playbackManager: PlaybackManager,
    private val mediaRepository: MediaRepository
) : ViewModel(), FoldersScreenActions {

    private val _state: StateFlow<FoldersScreenState> = foldersRepository.basicFolders
        .combine(
            userPreferencesRepository.librarySettingsFlow.map { it.foldersSortOrder }
                .stateIn(viewModelScope, SharingStarted.Eagerly, FoldersSortOption.NAME to true)
        ) { folders, sortOption ->
            val sortedFolders = folders
                .let {
                    val sortProperty: Comparator<BasicAlbum> = when (sortOption.first) {
                        FoldersSortOption.NAME -> compareBy { it.albumInfo.name }
                        FoldersSortOption.NUMBER_OF_SONGS -> compareBy { it.albumInfo.numberOfSongs }
                    }
                    if (sortOption.second)
                        it.sortedWith(sortProperty)
                    else
                        it.sortedWith(sortProperty).reversed()
                }
            FoldersScreenState(sortedFolders)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            FoldersScreenState(foldersRepository.basicFolders.value)
        )

    val state: StateFlow<FoldersScreenState>
        get() = _state

    override fun changeGridSize(newSize: Int) {
        viewModelScope.launch {
            userPreferencesRepository.changeFoldersGridSize(newSize)
        }
    }

    override fun changeSortOptions(sortOption: FoldersSortOption, isAscending: IsAscending) {
        viewModelScope.launch {
            userPreferencesRepository.changeFoldersSortOrder(sortOption, isAscending)
        }
    }

    override fun playFolders(folderNames: List<BasicAlbum>) {
        val songs = getFoldersSongs(folderNames.map { it.albumInfo.name })
        playbackManager.setPlaylistAndPlayAtIndex(songs, 0)
    }

    override fun playFoldersNext(folderNames: List<BasicAlbum>) {
        val songs = getFoldersSongs(folderNames.map { it.albumInfo.name })
        playbackManager.playNext(songs)
    }

    override fun addFoldersToQueue(folderNames: List<BasicAlbum>) {
        val songs = getFoldersSongs(folderNames.map { it.albumInfo.name })
        playbackManager.addToQueue(songs)
    }

    override fun shuffleFolders(folderNames: List<BasicAlbum>) {
        val songs = getFoldersSongs(folderNames.map { it.albumInfo.name })
        playbackManager.shuffle(songs)
    }

    override fun shuffleFoldersNext(folderNames: List<BasicAlbum>) {
        val songs = getFoldersSongs(folderNames.map { it.albumInfo.name })
        playbackManager.shuffleNext(songs)
    }

    override fun addFoldersToPlaylist(folderNames: List<BasicAlbum>, playlistName: String) {
        TODO("Not yet implemented")
    }

    private fun getFolderSongs(folderName: String): List<Song> {
        return mediaRepository.songsFlow.value.songs
            .filter { getParentFolderName(it.filePath) == folderName }
    }

    private fun getFoldersSongs(folderNames: List<String>): List<Song> {
        return folderNames
            .map { getFolderSongs(it) }
            .flatten()
    }

    /**
     * 从文件路径提取父文件夹名
     * "/sdcard/Music/周杰伦/歌曲.mp3" -> "周杰伦"
     */
    private fun getParentFolderName(filePath: String): String {
        return try {
            File(filePath).parentFile?.name ?: "Unknown Folder"
        } catch (e: Exception) {
            "Unknown Folder"
        }
    }

}