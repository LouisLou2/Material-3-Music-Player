package com.omar.musica.artists.ui.artistsscreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
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
fun ArtistsList(
    modifier: Modifier,
    artists: List<BasicAlbum>,
    multiSelectState: MultiSelectState<BasicAlbum>,
    onArtistClicked: (BasicAlbum) -> Unit,
    onArtistLongClicked: (BasicAlbum) -> Unit
) {
    val listState = rememberLazyListState()
    LazyColumn(modifier, state = listState) {
        items(artists, key = { it.albumInfo.name }) {
            val isSelected = multiSelectState.selected.contains(it)
            ArtistRow(
                modifier = Modifier
                    .animateItemPlacement()
                    .fillMaxWidth()
                    .combinedClickable(
                        onLongClick = { onArtistLongClicked(it) },
                        onClick = { onArtistClicked(it) }
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
                artist = it,
                isSelected = isSelected
            )
            if (it != artists.last()) {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = (56 + 8 + 12).dp)
                )
            }
        }


    }
    LaunchedEffect(key1 = artists) {
        listState.scrollToItem(0)
    }
} 