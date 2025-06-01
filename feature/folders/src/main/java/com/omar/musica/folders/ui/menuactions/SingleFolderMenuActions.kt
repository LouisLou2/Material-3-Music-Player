package com.omar.musica.folders.ui.menuactions

import com.omar.musica.ui.menu.MenuActionItem
import com.omar.musica.ui.menu.addShortcutToHomeScreen
import com.omar.musica.ui.menu.addToPlaylists
import com.omar.musica.ui.menu.addToQueue
import com.omar.musica.ui.menu.playNext
import com.omar.musica.ui.menu.shuffleNext


fun buildSingleFolderMenuActions(
    onPlayNext: () -> Unit,
    addToQueue: () -> Unit,
    onShuffleNext: () -> Unit,
    onAddToPlaylists: () -> Unit,
    onCreateShortcut: () -> Unit,
): List<MenuActionItem> {
    return mutableListOf<MenuActionItem>()
        .apply {
            playNext(onPlayNext)
            addToQueue(addToQueue)
            shuffleNext(onShuffleNext)
            addToPlaylists(onAddToPlaylists)
            addShortcutToHomeScreen(onCreateShortcut)
        }
}