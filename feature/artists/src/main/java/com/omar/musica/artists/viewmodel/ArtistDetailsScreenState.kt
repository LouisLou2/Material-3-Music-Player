package com.omar.musica.artists.viewmodel

import androidx.compose.runtime.Stable
import com.omar.musica.store.model.album.AlbumWithSongs
import com.omar.musica.store.model.album.BasicAlbum


sealed interface ArtistDetailsScreenState {
    data object Loading : ArtistDetailsScreenState

    @Stable
    data class Loaded(
        val artistWithSongs: AlbumWithSongs,
        val otherInfo: List<BasicAlbum>
    ) : ArtistDetailsScreenState
} 