package com.omar.musica.artists.viewmodel

import com.omar.musica.model.ArtistsSortOption
import com.omar.musica.model.prefs.IsAscending
import com.omar.musica.store.model.album.BasicAlbum

interface ArtistsScreenActions {

    fun changeGridSize(newSize: Int)
    fun changeSortOptions(sortOption: ArtistsSortOption, isAscending: IsAscending)

    fun playArtists(artistNames: List<BasicAlbum>)
    fun playArtistsNext(artistNames: List<BasicAlbum>)
    fun addArtistsToQueue(artistNames: List<BasicAlbum>)

    fun shuffleArtists(artistNames: List<BasicAlbum>)
    fun shuffleArtistsNext(artistNames: List<BasicAlbum>)

    fun addArtistsToPlaylist(artistNames: List<BasicAlbum>, playlistName: String)
} 