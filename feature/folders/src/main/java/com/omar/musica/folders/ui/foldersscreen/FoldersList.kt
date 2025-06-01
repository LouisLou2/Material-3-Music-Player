package com.omar.musica.folders.ui.foldersscreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.omar.musica.store.model.album.BasicAlbum
import com.omar.musica.ui.common.MultiSelectState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FoldersList(
    modifier: Modifier,
    folders: List<BasicAlbum>,
    multiSelectState: MultiSelectState<BasicAlbum>,
    onFolderClicked: (BasicAlbum) -> Unit,
    onFolderLongClicked: (BasicAlbum) -> Unit
) {
    val listState = rememberLazyListState()
    LazyColumn(modifier, state = listState) {
        items(folders, key = { it.albumInfo.name }) {
            val isSelected = multiSelectState.selected.contains(it)
            FolderItem(
                modifier = Modifier
                    .animateItemPlacement()
                    .fillMaxWidth()
                    .combinedClickable(
                        onLongClick = { onFolderLongClicked(it) },
                        onClick = { onFolderClicked(it) }
                    )
                    .then(
                        if (isSelected) Modifier.background(
                            MaterialTheme.colorScheme.surfaceContainerHighest.copy(
                                alpha = 0.7f
                            )
                        )
                        else Modifier
                    )
                    .padding(top = 12.dp, bottom = 12.dp, start = 12.dp, end = 12.dp),
                folder = it,
                isSelected = isSelected,
                onFolderClicked = onFolderClicked,
                onFolderLongClicked = onFolderLongClicked
            )
            if (it != folders.last()) {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = (56 + 8 + 12).dp)
                )
            }
        }
    }
    LaunchedEffect(key1 = folders) {
        listState.scrollToItem(0)
    }
}
