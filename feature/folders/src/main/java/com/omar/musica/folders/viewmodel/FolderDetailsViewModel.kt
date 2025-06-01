package com.omar.musica.folders.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.musica.folders.ui.folderdetail.FolderDetailActions
import com.omar.musica.playback.PlaybackManager
import com.omar.musica.store.FoldersRepository
import com.omar.musica.store.model.song.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class FolderDetailsViewModel @Inject constructor(
    private val foldersRepository: FoldersRepository,
    savedStateHandle: SavedStateHandle,
    private val playbackManager: PlaybackManager
) : ViewModel(), FolderDetailActions {

    private val folderId = savedStateHandle.get<Int>(FOLDER_ID_KEY)!!

    val state: StateFlow<FolderDetailsScreenState> =
        combine(
            foldersRepository.getFolderWithSongs(folderId),
            foldersRepository.basicFolders
        ) { folder, allFolders ->
            if (folder == null) return@combine FolderDetailsScreenState.Loading
            
            // 获取其他文件夹（排除当前文件夹，最多显示6个）
            val otherFolders = allFolders
                .filter { it.albumInfo.id != folderId }
                .take(6)
            
            FolderDetailsScreenState.Loaded(
                folderWithSongs = folder,
                otherFolders = otherFolders
            )
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, FolderDetailsScreenState.Loading)


    override fun play() {
        playbackManager.setPlaylistAndPlayAtIndex(getFolderSongs())
    }

    override fun playAtIndex(index: Int) {
        playbackManager.setPlaylistAndPlayAtIndex(getFolderSongs(), index)
    }

    override fun playNext() {
        playbackManager.playNext(getFolderSongs())
    }

    override fun shuffle() {
        playbackManager.shuffle(getFolderSongs())
    }

    override fun shuffleNext() {
        playbackManager.shuffleNext(getFolderSongs())
    }

    override fun addToQueue() {
        playbackManager.addToQueue(getFolderSongs())
    }

    private fun getFolderSongs(): List<Song> {
        val currentState = state.value
        return if (currentState is FolderDetailsScreenState.Loaded) {
            currentState.folderWithSongs.songs.map { it.song }
        } else {
            emptyList()
        }
    }

    companion object {
        // Make sure this matches the key used in FoldersNavigation.kt
        const val FOLDER_ID_KEY = "folderId"
    }

}