package com.omar.musica.folders.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import androidx.navigation.navigation
import com.omar.musica.folders.ui.folderdetail.FolderDetailsScreen
import com.omar.musica.folders.ui.foldersscreen.FoldersScreen
import com.omar.musica.folders.viewmodel.FolderDetailsViewModel


const val FOLDERS_NAVIGATION_GRAPH = "folders_graph"
const val FOLDERS_ROUTE = "folders"
const val FOLDER_DETAIL_ROUTE = "folder/{${FolderDetailsViewModel.FOLDER_ID_KEY}}"


fun NavController.navigateToFolders(navOptions: NavOptions? = null) {
    navigate(FOLDERS_NAVIGATION_GRAPH, navOptions)
}

fun NavController.navigateToFolderDetail(folderId: Int, navOptions: NavOptions? = null) {
    navigate("folder/$folderId", navOptions)
}

fun NavGraphBuilder.foldersGraph(
    contentModifier: MutableState<Modifier>,
    navController: NavController,
    enableBackPress: MutableState<Boolean>,
    enterAnimationFactory:
        (String, AnimatedContentTransitionScope<NavBackStackEntry>) -> EnterTransition,
    exitAnimationFactory:
        (String, AnimatedContentTransitionScope<NavBackStackEntry>) -> ExitTransition,
    popEnterAnimationFactory:
        (String, AnimatedContentTransitionScope<NavBackStackEntry>) -> EnterTransition,
    popExitAnimationFactory:
        (String, AnimatedContentTransitionScope<NavBackStackEntry>) -> ExitTransition,
) {

    navigation(
        route = FOLDERS_NAVIGATION_GRAPH,
        startDestination = FOLDERS_ROUTE,
    ) {
        composable(
            FOLDERS_ROUTE,
            enterTransition = {
                enterAnimationFactory(FOLDERS_ROUTE, this)
            },
            exitTransition = {
                exitAnimationFactory(FOLDERS_ROUTE, this)
            },
            popEnterTransition = {
                popEnterAnimationFactory(FOLDERS_ROUTE, this)
            },
            popExitTransition = {
                popExitAnimationFactory(FOLDERS_ROUTE, this)
            }
        ) {
            FoldersScreen(
                modifier = contentModifier.value,
                onFolderClicked = { folderId -> navController.navigateToFolderDetail(folderId) }
            )
        }

        composable(
            FOLDER_DETAIL_ROUTE,
            enterTransition = {
                enterAnimationFactory(FOLDER_DETAIL_ROUTE, this)
            },
            popExitTransition = {
                popExitAnimationFactory(FOLDER_DETAIL_ROUTE, this)
            },
            arguments = listOf(
                navArgument(FolderDetailsViewModel.FOLDER_ID_KEY) {
                    type = NavType.IntType
                }
            )
        )
        {
            FolderDetailsScreen(
                modifier = contentModifier.value,
                onBackClicked = { navController.popBackStack() },
                onNavigateToFolder = { folderId -> navController.navigateToFolderDetail(folderId) }
            )
        }
    }

} 