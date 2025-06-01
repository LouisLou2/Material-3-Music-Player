package com.omar.musica.artists.ui.artistsscreen

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
import com.omar.musica.artists.ui.menuactions.buildArtistsMenuActions
import com.omar.musica.artists.viewmodel.ArtistsScreenActions
import com.omar.musica.artists.viewmodel.ArtistsScreenState
import com.omar.musica.artists.viewmodel.ArtistsViewModel
import com.omar.musica.store.model.album.BasicAlbum
import com.omar.musica.ui.anim.OPEN_SCREEN_ENTER_ANIMATION
import com.omar.musica.ui.anim.POP_SCREEN_EXIT_ANIMATION
import com.omar.musica.ui.common.LocalUserPreferences
import com.omar.musica.ui.common.MultiSelectState
import com.omar.musica.ui.topbar.SelectionTopAppBarScaffold


@Composable
fun ArtistsScreen(
    modifier: Modifier,
    onArtistClicked: (artistId: Int) -> Unit,
    viewModel: ArtistsViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()

    ArtistsScreen(
        modifier = modifier,
        state = state,
        actions = viewModel,
        onArtistClicked = onArtistClicked,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistsScreen(
    modifier: Modifier,
    state: ArtistsScreenState,
    actions: ArtistsScreenActions,
    onArtistClicked: (artistId: Int) -> Unit
) {

    val artists = state.artists

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
                actionItems = buildArtistsMenuActions(
                    onPlay = {
                        actions.playArtists(multiSelectState.selected)
                    },
                    addToQueue = {
                        actions.addArtistsToQueue(multiSelectState.selected)
                    },
                    onPlayNext = {
                        actions.playArtistsNext(multiSelectState.selected)
                    },
                    onShuffle = {
                        actions.shuffleArtists(multiSelectState.selected)
                    },
                    onShuffleNext = {
                        actions.shuffleArtistsNext(multiSelectState.selected)
                    },
                    onAddToPlaylists = {

                    }
                ),
                numberOfVisibleIcons = 2,
                scrollBehavior = topAppBarScrollBehavior,
            ) {
                ArtistsTopBar(
                    scrollBehavior = topAppBarScrollBehavior,
                    gridSize = librarySettings.artistsGridSize,
                    sortOrder = librarySettings.artistsSortOrder,
                    { actions.changeSortOptions(it.first, it.second) },
                    actions::changeGridSize
                )
            }
        },
        modifier = modifier,
    ) { paddingValues ->

        AnimatedContent(
            targetState = librarySettings.artistsGridSize, label = "",
            transitionSpec = {
                OPEN_SCREEN_ENTER_ANIMATION togetherWith POP_SCREEN_EXIT_ANIMATION
            }
        ) {
            when (it) {
                1 -> {
                    ArtistsList(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
                        artists = artists,
                        multiSelectState = multiSelectState,
                        onArtistClicked = {
                            if (multiSelectEnabled) multiSelectState.toggle(it)
                            else {
                                onArtistClicked(it.albumInfo.id)
                            }
                        },
                        onArtistLongClicked = {
                            multiSelectState.toggle(it)
                        }
                    )
                }

                else -> {
                    ArtistsGrid(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
                        artists = artists,
                        numOfColumns = it,
                        multiSelectState = multiSelectState,
                        onArtistClicked = {
                            if (multiSelectEnabled) multiSelectState.toggle(it)
                            else {
                                onArtistClicked(it.albumInfo.id)
                            }
                        },
                        onArtistLongClicked = {
                            multiSelectState.toggle(it)
                        }
                    )
                }
            }
        }
    }


} 