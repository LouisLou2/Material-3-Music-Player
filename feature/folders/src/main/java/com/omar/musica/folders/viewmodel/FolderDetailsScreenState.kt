package com.omar.musica.folders.viewmodel

import androidx.compose.runtime.Stable
import com.omar.musica.store.model.album.AlbumWithSongs
import com.omar.musica.store.model.album.BasicAlbum


sealed interface FolderDetailsScreenState {
    data object Loading : FolderDetailsScreenState

    @Stable
    data class Loaded(
        val folderWithSongs: AlbumWithSongs,
        val otherFolders: List<BasicAlbum> = emptyList()
    ) : FolderDetailsScreenState
}