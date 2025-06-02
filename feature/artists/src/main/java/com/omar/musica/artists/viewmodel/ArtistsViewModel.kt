package com.omar.musica.artists.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.musica.model.ArtistsSortOption
import com.omar.musica.model.prefs.IsAscending
import com.omar.musica.playback.PlaybackManager
import com.omar.musica.store.ArtistsRepository
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
import javax.inject.Inject


@HiltViewModel
class ArtistsViewModel @Inject constructor(
    private val artistsRepository: ArtistsRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val playbackManager: PlaybackManager,
    private val mediaRepository: MediaRepository
) : ViewModel(), ArtistsScreenActions {


    private val _state: StateFlow<ArtistsScreenState> = artistsRepository.basicArtists
        .combine(
            userPreferencesRepository.librarySettingsFlow.map { it.artistsSortOrder }
                .stateIn(viewModelScope, SharingStarted.Eagerly, ArtistsSortOption.NAME to true)
        ) { artists, sortOption ->
            val sortedArtists = artists
                .let {
                    val sortProperty: Comparator<BasicAlbum> = when (sortOption.first) {
                        ArtistsSortOption.NAME -> compareBy { it.albumInfo.name }
                        ArtistsSortOption.NUMBER_OF_SONGS -> compareBy { it.albumInfo.numberOfSongs }
                    }
                    if (sortOption.second)
                        it.sortedWith(sortProperty)
                    else
                        it.sortedWith(sortProperty).reversed()
                }
            ArtistsScreenState(sortedArtists)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            ArtistsScreenState(artistsRepository.basicArtists.value)
        )

    val state: StateFlow<ArtistsScreenState>
        get() = _state


    override fun changeGridSize(newSize: Int) {
        viewModelScope.launch {
            userPreferencesRepository.changeArtistsGridSize(newSize)
        }
    }

    override fun changeSortOptions(sortOption: ArtistsSortOption, isAscending: IsAscending) {
        viewModelScope.launch {
            userPreferencesRepository.changeArtistsSortOrder(sortOption, isAscending)
        }
    }

    override fun playArtists(artistNames: List<BasicAlbum>) {
        val songs = getArtistsSongs(artistNames.map { it.albumInfo.name })
        playbackManager.setPlaylistAndPlayAtIndex(songs, 0)
    }

    override fun playArtistsNext(artistNames: List<BasicAlbum>) {
        val songs = getArtistsSongs(artistNames.map { it.albumInfo.name })
        playbackManager.playNext(songs)
    }

    override fun addArtistsToQueue(artistNames: List<BasicAlbum>) {
        val songs = getArtistsSongs(artistNames.map { it.albumInfo.name })
        playbackManager.addToQueue(songs)
    }

    override fun shuffleArtists(artistNames: List<BasicAlbum>) {
        val songs = getArtistsSongs(artistNames.map { it.albumInfo.name })
        playbackManager.shuffle(songs)
    }

    override fun shuffleArtistsNext(artistNames: List<BasicAlbum>) {
        val songs = getArtistsSongs(artistNames.map { it.albumInfo.name })
        playbackManager.shuffleNext(songs)
    }

    override fun addArtistsToPlaylist(artistNames: List<BasicAlbum>, playlistName: String) {
        TODO("Not yet implemented")
    }

    private fun getArtistSongs(artistName: String): List<Song> {
        return mediaRepository.songsFlow.value.songs
            .filter { it.metadata.artistName == artistName }
    }

    private fun getArtistsSongs(artistNames: List<String>): List<Song> {
        return artistNames
            .map { getArtistSongs(it) }
            .flatten()
    }

} 