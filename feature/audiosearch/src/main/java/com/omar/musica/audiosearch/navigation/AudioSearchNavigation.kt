package com.omar.musica.audiosearch.navigation

import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.omar.musica.audiosearch.ui.AudioSearchScreen

/**
 * 听歌识曲模块的导航常量
 */
const val AUDIO_SEARCH_ROUTE = "audio_search"

/**
 * 导航到听歌识曲界面
 */
fun NavController.navigateToAudioSearch() {
    this.navigate(AUDIO_SEARCH_ROUTE)
}

/**
 * 添加听歌识曲导航图到NavGraphBuilder
 * 
 * @param contentModifier 内容修饰符，用于统一的界面样式
 * @param navController 导航控制器
 */
fun NavGraphBuilder.audioSearchGraph(
    contentModifier: MutableState<Modifier>,
    navController: NavController
) {
    composable(route = AUDIO_SEARCH_ROUTE) {
        AudioSearchScreen(
            modifier = contentModifier.value
        )
    }
} 