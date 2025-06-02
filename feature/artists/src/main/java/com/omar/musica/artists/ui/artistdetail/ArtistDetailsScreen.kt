package com.omar.musica.artists.ui.artistdetail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.omar.musica.artists.viewmodel.ArtistDetailsScreenState
import com.omar.musica.artists.viewmodel.ArtistDetailsViewModel
import com.omar.musica.store.model.album.AlbumSong
import com.omar.musica.ui.albumart.SongAlbumArtImage
import com.omar.musica.ui.albumart.toSongAlbumArtModel
import com.omar.musica.ui.common.LocalCommonSongsAction
import com.omar.musica.ui.common.MultiSelectState
import com.omar.musica.ui.menu.buildCommonMultipleSongsActions
import com.omar.musica.ui.topbar.SelectionTopAppBarScaffold


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistDetailsScreen(
    modifier: Modifier,
    viewModel: ArtistDetailsViewModel = hiltViewModel(),
    onNavigateToArtist: (artistId: Int) -> Unit,
    onBackClicked: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    
    when (val currentState = state) {
        is ArtistDetailsScreenState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        
        is ArtistDetailsScreenState.Loaded -> {
            ArtistDetailsLoadedScreen(
                modifier = modifier,
                state = currentState,
                actions = viewModel,
                onBackClicked = onBackClicked
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ArtistDetailsLoadedScreen(
    modifier: Modifier,
    state: ArtistDetailsScreenState.Loaded,
    actions: ArtistDetailActions,
    onBackClicked: () -> Unit
) {
    val artistInfo = state.artistWithSongs.albumInfo
    val artistSongs = state.artistWithSongs.songs
    val artistName = artistInfo.name

    val multiSelectState = remember { MultiSelectState<AlbumSong>() }
    val context = LocalContext.current
    val localCommonSongActions = LocalCommonSongsAction.current

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = artistName,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        
        SelectionTopAppBarScaffold(
            modifier = Modifier.fillMaxWidth(),
            multiSelectState = multiSelectState,
            isMultiSelectEnabled = multiSelectState.selected.isNotEmpty(),
            actionItems = buildCommonMultipleSongsActions(
                multiSelectState.selected.map { it.song },
                context,
                localCommonSongActions.playbackActions,
                localCommonSongActions.addToPlaylistDialog,
                localCommonSongActions.shareAction
            ),
            numberOfVisibleIcons = 3
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // 艺术家头部信息
                item {
                    ArtistHeader(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        artistName = artistName,
                        songCount = artistInfo.numberOfSongs,
                        firstSong = artistSongs.firstOrNull()?.song
                    )
                }

                // 播放按钮
                item {
                    ArtistPlaybackButtons(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        onPlay = actions::play,
                        onShuffle = actions::shuffle
                    )
                }

                // Songs标题
                item {
                    Text(
                        modifier = Modifier.padding(
                            start = 16.dp,
                            top = 16.dp,
                            bottom = 8.dp
                        ),
                        text = "Songs",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Light
                    )
                }

                // 歌曲列表
                itemsIndexed(artistSongs) { index, song ->
                    ArtistSongRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .combinedClickable(
                                onLongClick = { multiSelectState.toggle(song) },
                                onClick = {
                                    if (multiSelectState.selected.isNotEmpty()) {
                                        multiSelectState.toggle(song)
                                    } else {
                                        actions.playAtIndex(index)
                                    }
                                }
                            )
                            .then(
                                if (multiSelectState.selected.contains(song))
                                    Modifier.background(
                                        MaterialTheme.colorScheme.onSurface.copy(0.15f)
                                    )
                                else Modifier
                            )
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        song = song,
                        trackNumber = index + 1
                    )
                }
            }
        }
    }
}

@Composable
fun ArtistHeader(
    modifier: Modifier,
    artistName: String,
    songCount: Int,
    firstSong: com.omar.musica.store.model.song.Song?
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 艺术家图片
        SongAlbumArtImage(
            modifier = Modifier
                .size(200.dp)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(16.dp)),
            songAlbumArtModel = firstSong.toSongAlbumArtModel(),
            crossFadeDuration = 300
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 艺术家名称
        Text(
            text = artistName,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 歌曲数量
        Text(
            text = "$songCount songs",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
} 