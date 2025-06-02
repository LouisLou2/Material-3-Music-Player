package com.omar.musica.folders.ui.foldersscreen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.navigation.compose.hiltViewModel
import com.omar.musica.folders.ui.menuactions.buildFoldersMenuActions
import com.omar.musica.folders.viewmodel.FoldersScreenActions
import com.omar.musica.folders.viewmodel.FoldersScreenState
import com.omar.musica.folders.viewmodel.FoldersViewModel
import com.omar.musica.store.model.album.BasicAlbum
import com.omar.musica.ui.anim.OPEN_SCREEN_ENTER_ANIMATION
import com.omar.musica.ui.anim.POP_SCREEN_EXIT_ANIMATION
import com.omar.musica.ui.common.LocalUserPreferences
import com.omar.musica.ui.common.MultiSelectState
import com.omar.musica.ui.topbar.SelectionTopAppBarScaffold


@Composable
fun FoldersScreen(
    modifier: Modifier,
    onFolderClicked: (folderId: Int) -> Unit,
    viewModel: FoldersViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()

    FoldersScreen(
        modifier = modifier,
        state = state,
        actions = viewModel,
        onFolderClicked = onFolderClicked,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoldersScreen(
    modifier: Modifier,
    state: FoldersScreenState,
    actions: FoldersScreenActions,
    onFolderClicked: (folderId: Int) -> Unit
) {

    val folders = state.folders

    val topAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val librarySettings = LocalUserPreferences.current.librarySettings

    val multiSelectState = remember {
        MultiSelectState<BasicAlbum>()
    }
    val multiSelectEnabled by remember {
        derivedStateOf { multiSelectState.selected.size > 0 }
    }

    BackHandler(multiSelectEnabled) {
        multiSelectState.clear()
    }

    Scaffold(
        topBar = {
            SelectionTopAppBarScaffold(
                modifier = Modifier.fillMaxWidth(),
                multiSelectState = multiSelectState,
                isMultiSelectEnabled = multiSelectEnabled,
                actionItems = buildFoldersMenuActions(
                    onPlay = {
                        actions.playFolders(multiSelectState.selected)
                    },
                    addToQueue = {
                        actions.addFoldersToQueue(multiSelectState.selected)
                    },
                    onPlayNext = {
                        actions.playFoldersNext(multiSelectState.selected)
                    },
                    onShuffle = {
                        actions.shuffleFolders(multiSelectState.selected)
                    },
                    onShuffleNext = {
                        actions.shuffleFoldersNext(multiSelectState.selected)
                    },
                    onAddToPlaylists = {

                    }
                ),
                numberOfVisibleIcons = 2,
                scrollBehavior = topAppBarScrollBehavior,
            ) {
                FoldersTopBar(
                    scrollBehavior = topAppBarScrollBehavior,
                    gridSize = librarySettings.foldersGridSize,
                    sortOrder = librarySettings.foldersSortOrder,
                    { actions.changeSortOptions(it.first, it.second) },
                    actions::changeGridSize
                )
            }
        },
        modifier = modifier,
    ) { paddingValues ->

        AnimatedContent(
            targetState = librarySettings.foldersGridSize, label = "",
            transitionSpec = {
                OPEN_SCREEN_ENTER_ANIMATION togetherWith POP_SCREEN_EXIT_ANIMATION
            }
        ) {
            when (it) {
                1 -> {
                    FoldersList(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
                        folders = folders,
                        multiSelectState = multiSelectState,
                        onFolderClicked = {
                            if (multiSelectEnabled) multiSelectState.toggle(it)
                            else {
                                onFolderClicked(it.albumInfo.id)
                            }
                        },
                        onFolderLongClicked = {
                            multiSelectState.toggle(it)
                        }
                    )
                }

                else -> {
                    FoldersGrid(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
                        folders = folders,
                        numOfColumns = it,
                        multiSelectState = multiSelectState,
                        onFolderClicked = {
                            if (multiSelectEnabled) multiSelectState.toggle(it)
                            else {
                                onFolderClicked(it.albumInfo.id)
                            }
                        },
                        onFolderLongClicked = {
                            multiSelectState.toggle(it)
                        }
                    )
                }
            }
        }
    }


} 