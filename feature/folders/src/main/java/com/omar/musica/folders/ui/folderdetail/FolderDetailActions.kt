package com.omar.musica.folders.ui.folderdetail

interface FolderDetailActions {
    fun play()
    fun playAtIndex(index: Int)
    fun playNext()
    fun shuffle()
    fun shuffleNext()
    fun addToQueue()
} 