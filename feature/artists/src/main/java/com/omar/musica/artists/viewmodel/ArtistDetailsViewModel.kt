package com.omar.musica.artists.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.musica.artists.ui.artistdetail.ArtistDetailActions
import com.omar.musica.playback.PlaybackManager
import com.omar.musica.store.ArtistsRepository
import com.omar.musica.store.model.song.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class ArtistDetailsViewModel @Inject constructor(
    artistsRepository: ArtistsRepository,
    savedStateHandle: SavedStateHandle,
    private val playbackManager: PlaybackManager
) : ViewModel(), ArtistDetailActions {

    private val artistId = savedStateHandle.get<Int>(ARTIST_ID_KEY)!!

    val state: StateFlow<ArtistDetailsScreenState> =
        artistsRepository.getArtistWithSongs(artistId).map { artist ->
            if (artist == null) return@map ArtistDetailsScreenState.Loading
            val artistName = artist.albumInfo.name // 歌手名在这里

            // 可以显示其他相关信息，比如该歌手的专辑等
            ArtistDetailsScreenState.Loaded(artist, emptyList())
        }
            .stateIn(viewModelScope, SharingStarted.Eagerly, ArtistDetailsScreenState.Loading)


    override fun play() {
        playbackManager.setPlaylistAndPlayAtIndex(getArtistSongs())
    }

    override fun playAtIndex(index: Int) {
        playbackManager.setPlaylistAndPlayAtIndex(getArtistSongs(), index)
    }

    override fun playNext() {
        playbackManager.playNext(getArtistSongs())
    }

    override fun shuffle() {
        playbackManager.shuffle(getArtistSongs())
    }

    override fun shuffleNext() {
        playbackManager.shuffleNext(getArtistSongs())
    }

    override fun addToQueue() {
        playbackManager.addToQueue(getArtistSongs())
    }

    private fun getArtistSongs(): List<Song> {
        return (state.value as? ArtistDetailsScreenState.Loaded)?.artistWithSongs?.songs?.map { it.song }
            ?: listOf()
    }

    companion object {
        const val ARTIST_ID_KEY = "ARTIST_ID"
    }

} 