package com.omar.musica.folders.ui.folderdetail

import android.app.Activity
import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.size.Size
import com.omar.musica.folders.ui.effects.FolderDetailStatusBarColorEffect
import com.omar.musica.folders.viewmodel.FolderDetailsScreenState
import com.omar.musica.folders.viewmodel.FolderDetailsViewModel
import com.omar.musica.model.album.BasicAlbumInfo
import com.omar.musica.store.model.album.AlbumSong
import com.omar.musica.ui.actions.rememberCreatePlaylistShortcutDialog
import com.omar.musica.ui.albumart.LocalInefficientThumbnailImageLoader
import com.omar.musica.ui.albumart.SongAlbumArtImage
import com.omar.musica.ui.albumart.SongAlbumArtModel
import com.omar.musica.ui.albumart.toSongAlbumArtModel
import com.omar.musica.ui.common.LocalCommonSongsAction
import com.omar.musica.ui.common.MultiSelectState
import com.omar.musica.ui.menu.buildCommonMultipleSongsActions
import com.omar.musica.ui.shortcut.ShortcutDialogData
import com.omar.musica.ui.showShortToast
import com.omar.musica.ui.theme.isAppInDarkTheme
import com.omar.musica.ui.topbar.SelectionTopAppBarScaffold
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun FolderDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: FolderDetailsViewModel = hiltViewModel(),
    onBackClicked: () -> Unit,
    onNavigateToFolder: (folderId: Int) -> Unit
) {
    val state by viewModel.state.collectAsState()
    if (state !is FolderDetailsScreenState.Loaded) {
        FolderDetailsLoadingScreen(Modifier.fillMaxSize())
        return
    }

    BoxWithConstraints {
        // 使用紧凑布局（类似albums的卡片式布局）
        FolderDetailsCompactScreen(
            modifier = modifier,
            state = state as FolderDetailsScreenState.Loaded,
            actions = viewModel,
            onBackClicked = onBackClicked,
            onNavigateToFolder = onNavigateToFolder
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FolderDetailsPortraitScreen(
    modifier: Modifier,
    state: FolderDetailsScreenState.Loaded,
    actions: FolderDetailActions,
    onBackClicked: () -> Unit,
    onNavigateToFolder: (folderId: Int) -> Unit
) {

    val folderInfo = state.folderWithSongs.albumInfo
    val folderSongs = state.folderWithSongs.songs
    val otherFolders = state.otherFolders

    val collapseSystem = remember { CollapsingSystem() }

    val density = LocalDensity.current

    if (collapseSystem.collapsePercentage < 0.4f)
        FolderDetailStatusBarColorEffect(collapsePercentage = collapseSystem.collapsePercentage)

    Box(modifier = modifier
        .onGloballyPositioned {
            collapseSystem.screenWidthPx = it.size.width
        }
        .nestedScroll(collapseSystem.nestedScrollConnection)
    ) {

        FolderArtHeader(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.0f)
                .offset {
                    val yOffset =
                        (-collapseSystem.collapsePercentage * 0.15 * collapseSystem.screenWidthPx).toInt()
                    IntOffset(0, yOffset)
                }
                .graphicsLayer {
                    alpha = (1 - collapseSystem.collapsePercentage * 2).coerceIn(0.0f, 1.0f)
                },
            songAlbumArtModel = folderSongs.firstOrNull()?.song.toSongAlbumArtModel(),
            albumInfo = folderInfo,
            fadeEdge = isAppInDarkTheme()
        )

        val multiSelectState = remember {
            MultiSelectState<AlbumSong>()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = with(density) { collapseSystem.topBarHeightPx.toDp() })
                .offset {
                    // offset by 8dp to show rounded corners
                    val yOffset =
                        ((collapseSystem.totalCollapsableHeightPx - 8.dp.toPx()) * (1 - collapseSystem.collapsePercentage)).toInt()
                    IntOffset(0, yOffset)
                }
                .then(
                    if (isAppInDarkTheme())
                        Modifier
                    else
                        Modifier.shadow(
                            (24 * (1 - collapseSystem.collapsePercentage)).dp,
                            spotColor = Color.Transparent
                        )
                )
                .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(12.dp))

        ) {
            Spacer(modifier = Modifier.height(14.dp))

            FolderPlaybackButtons(
                modifier = Modifier.padding(horizontal = 16.dp),
                onPlay = actions::play,
                onShuffle = actions::shuffle
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {

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
                itemsIndexed(folderSongs) { num, song ->
                    FolderSongRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickableAndSelectable(
                                multiSelectState,
                                song
                            ) { actions.playAtIndex(num) }
                            .then(
                                if (multiSelectState.selected.contains(song))
                                    Modifier.background(
                                        MaterialTheme.colorScheme.onSurface.copy(
                                            0.15f
                                        )
                                    )
                                else
                                    Modifier
                            )
                            .padding(start = 24.dp, end = 6.dp, top = 4.dp, bottom = 4.dp),
                        song = song,
                        number = num + 1
                    )
                }
                if (otherFolders.isEmpty()) return@LazyColumn
                item {
                    Text(
                        modifier = Modifier.padding(
                            start = 16.dp,
                            top = 16.dp,
                            bottom = 8.dp
                        ),
                        text = "Other folders",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Light
                    )
                }
                item {
                    OtherFoldersRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min),
                        otherFolders = otherFolders,
                        onFolderClicked = onNavigateToFolder
                    )
                }
            }
        }

        val localCommonSongActions = LocalCommonSongsAction.current
        val addToPlaylistDialog = localCommonSongActions.addToPlaylistDialog

        val context = LocalContext.current
        val scope = rememberCoroutineScope()
        val createShortcutDialog = LocalCommonSongsAction.current.createShortcutDialog
        val imageLoader = LocalInefficientThumbnailImageLoader.current

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
            FolderDetailPortraitTopBar(
                modifier = Modifier.fillMaxWidth(),
                name = folderInfo.name,
                collapseSystem.collapsePercentage,
                onBarHeightChanged = { collapseSystem.topBarHeightPx = it },
                onBackClicked = onBackClicked,
                onPlayNext = {
                    actions.playNext()
                    context.showShortToast("${folderInfo.name} will play next")
                },
                onAddToQueue = {
                    actions.addToQueue()
                    context.showShortToast("${folderInfo.name} added to queue")
                },
                onShuffleNext = {
                    actions.shuffleNext()
                    context.showShortToast("${folderInfo.name} will play next")
                },
                onAddToPlaylists = {
                    addToPlaylistDialog.launch(folderSongs.map { it.song })
                },
                onOpenShortcutDialog = {
                    scope.launch {
                        // get bitmap
                        val request = ImageRequest.Builder(context)
                            .data(folderSongs.first().song.toSongAlbumArtModel())
                            .size(Size.ORIGINAL)
                            .build()

                        val result = withContext(Dispatchers.IO) { imageLoader.execute(request) }
                        val bitmap = if (result is SuccessResult)
                            (result.drawable as BitmapDrawable).bitmap
                        else
                            null

                        createShortcutDialog.launchForAlbum(
                            ShortcutDialogData.AlbumShortcutDialogData(
                                folderInfo.name,
                                folderInfo.id,
                                bitmap
                            )
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun FolderDetailsLandscapeScreen(
    modifier: Modifier,
    state: FolderDetailsScreenState.Loaded,
    actions: FolderDetailActions,
    onBackClicked: () -> Unit,
    onNavigateToFolder: (folderId: Int) -> Unit
) {
    // 横屏模式的实现 - 暂时使用竖屏模式的实现
    FolderDetailsPortraitScreen(
        modifier = modifier,
        state = state,
        actions = actions,
        onBackClicked = onBackClicked,
        onNavigateToFolder = onNavigateToFolder
    )
}

@OptIn(ExperimentalFoundationApi::class)
fun <T> Modifier.clickableAndSelectable(
    multiSelectState: MultiSelectState<T>,
    item: T,
    onNormalClick: () -> Unit
): Modifier = this.combinedClickable(
    onLongClick = {
        multiSelectState.toggle(item)
    },
    onClick = {
        if (multiSelectState.selected.isNotEmpty())
            multiSelectState.toggle(item)
        else
            onNormalClick()
    }
)

@Composable
fun FolderDetailsLoadingScreen(modifier: Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FolderDetailsCompactScreen(
    modifier: Modifier,
    state: FolderDetailsScreenState.Loaded,
    actions: FolderDetailActions,
    onBackClicked: () -> Unit,
    onNavigateToFolder: (folderId: Int) -> Unit
) {
    val folderInfo = state.folderWithSongs.albumInfo
    val folderSongs = state.folderWithSongs.songs
    val otherFolders = state.otherFolders

    val multiSelectState = remember {
        MultiSelectState<AlbumSong>()
    }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val localCommonSongActions = LocalCommonSongsAction.current

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = folderInfo.name) },
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
                // 封面和文件夹信息头部
                item {
                    FolderInfoHeader(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        songAlbumArtModel = folderSongs.firstOrNull()?.song.toSongAlbumArtModel(),
                        folderInfo = folderInfo,
                        onPlay = actions::play,
                        onShuffle = actions::shuffle
                    )
                }

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
                
                itemsIndexed(folderSongs) { num, song ->
                    FolderSongRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickableAndSelectable(
                                multiSelectState,
                                song
                            ) { actions.playAtIndex(num) }
                            .then(
                                if (multiSelectState.selected.contains(song))
                                    Modifier.background(
                                        MaterialTheme.colorScheme.onSurface.copy(
                                            0.15f
                                        )
                                    )
                                else
                                    Modifier
                            )
                            .padding(start = 24.dp, end = 6.dp, top = 4.dp, bottom = 4.dp),
                        song = song,
                        number = num + 1
                    )
                }
                
                if (otherFolders.isNotEmpty()) {
                    item {
                        Text(
                            modifier = Modifier.padding(
                                start = 16.dp,
                                top = 16.dp,
                                bottom = 8.dp
                            ),
                            text = "Other folders",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Light
                        )
                    }
                    item {
                        OtherFoldersRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min),
                            otherFolders = otherFolders,
                            onFolderClicked = onNavigateToFolder
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FolderInfoHeader(
    modifier: Modifier,
    songAlbumArtModel: SongAlbumArtModel,
    folderInfo: BasicAlbumInfo,
    onPlay: () -> Unit,
    onShuffle: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 封面图片 - 较小的卡片式布局
        SongAlbumArtImage(
            modifier = Modifier
                .size(200.dp)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(16.dp)),
            songAlbumArtModel = songAlbumArtModel,
            crossFadeDuration = 300
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 文件夹名称
        Text(
            text = folderInfo.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 歌曲数量
        Text(
            text = "${folderInfo.numberOfSongs} songs",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 播放按钮
        FolderPlaybackButtons(
            modifier = Modifier.fillMaxWidth(),
            onPlay = onPlay,
            onShuffle = onShuffle
        )
    }
} 