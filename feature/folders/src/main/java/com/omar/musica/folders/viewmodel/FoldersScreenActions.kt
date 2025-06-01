package com.omar.musica.folders.viewmodel

import com.omar.musica.model.FoldersSortOption
import com.omar.musica.model.prefs.IsAscending
import com.omar.musica.store.model.album.BasicAlbum

interface FoldersScreenActions {

    fun changeGridSize(newSize: Int)

    fun changeSortOptions(sortOption: FoldersSortOption, isAscending: IsAscending)

    fun playFolders(folderNames: List<BasicAlbum>)

    fun playFoldersNext(folderNames: List<BasicAlbum>)

    fun addFoldersToQueue(folderNames: List<BasicAlbum>)

    fun shuffleFolders(folderNames: List<BasicAlbum>)

    fun shuffleFoldersNext(folderNames: List<BasicAlbum>)

    fun addFoldersToPlaylist(folderNames: List<BasicAlbum>, playlistName: String)

}