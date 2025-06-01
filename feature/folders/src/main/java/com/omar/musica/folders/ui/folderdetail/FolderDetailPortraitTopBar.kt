package com.omar.musica.folders.ui.folderdetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextOverflow
import com.omar.musica.folders.ui.menuactions.buildSingleFolderMenuActions
import com.omar.musica.ui.topbar.OverflowMenu
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FolderDetailPortraitTopBar(
    modifier: Modifier,
    name: String,
    collapsePercentage: Float,
    onBarHeightChanged: (Int) -> Unit,
    onBackClicked: () -> Unit,
    onPlayNext: () -> Unit = {},
    onAddToQueue: () -> Unit = {},
    onShuffleNext: () -> Unit = {},
    onAddToPlaylists: () -> Unit = {},
    onOpenShortcutDialog: () -> Unit = {},
) {


    val scrimColor = MaterialTheme.colorScheme.surfaceDim
        .copy(alpha = 0.3f)

    val scrimModifier = Modifier
        .clip(CircleShape)
        .drawBehind {
            drawRect(
                scrimColor,
                alpha = 1 - collapsePercentage
            )
        }

    TopAppBar(
        modifier = modifier
            .onGloballyPositioned {
                onBarHeightChanged(it.size.height)
            },

        title = {
            Text(
                modifier = Modifier.graphicsLayer {
                    alpha = collapsePercentage
                },
                text = name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },

        navigationIcon = {
            IconButton(
                modifier = scrimModifier,
                onClick = onBackClicked
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = ""
                )
            }
        },

        actions = {
            CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface) {
                Box(modifier = scrimModifier) {
                    OverflowMenu(
                        actionItems = buildSingleFolderMenuActions(
                            onPlayNext,
                            onAddToQueue,
                            onShuffleNext,
                            onAddToPlaylists,
                            onOpenShortcutDialog
                        )
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
    )

}