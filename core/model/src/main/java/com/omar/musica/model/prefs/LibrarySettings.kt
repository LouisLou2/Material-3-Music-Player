package com.omar.musica.model.prefs

import com.omar.musica.model.AlbumsSortOption
import com.omar.musica.model.ArtistsSortOption
import com.omar.musica.model.FoldersSortOption
import com.omar.musica.model.SongSortOption


typealias IsAscending = Boolean

/**
 * Settings applied to the library and main screen.
 */
data class LibrarySettings(

    /**
     * The order of the songs on the main screen
     */
    val songsSortOrder: Pair<SongSortOption, IsAscending>,

    /**
     * The order of the songs on the main screen
     */
    val albumsSortOrder: Pair<AlbumsSortOption, IsAscending>,

    /**
     * The order of the artists on the main screen
     */
    val artistsSortOrder: Pair<ArtistsSortOption, IsAscending>,

    /**
     * The order of the folders on the main screen
     */
    val foldersSortOrder: Pair<FoldersSortOption, IsAscending>,

    /**
     * How many columns to show in the album
     * screen
     */
    val albumsGridSize: Int = 2,

    /**
     * How many columns to show in the artists
     * screen
     */
    val artistsGridSize: Int = 2,

    /**
     * How many columns to show in the folders
     * screen
     */
    val foldersGridSize: Int = 2,

    /**
     * Whether to load the actual album art of the song or
     * to cache album arts of songs of the same album
     */
    val cacheAlbumCoverArt: Boolean,

    /**
     * Files in these folders should not appear in the app
     */
    val excludedFolders: List<String>
)
