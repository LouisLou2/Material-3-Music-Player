package com.omar.musica.artists.ui.artistdetail

interface ArtistDetailActions {
    fun play()
    fun playAtIndex(index: Int)
    fun playNext()
    fun shuffle()
    fun shuffleNext()
    fun addToQueue()
} 