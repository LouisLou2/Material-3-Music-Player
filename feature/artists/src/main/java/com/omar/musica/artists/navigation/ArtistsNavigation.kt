package com.omar.musica.artists.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.omar.musica.artists.ui.artistdetail.ArtistDetailsScreen
import com.omar.musica.artists.ui.artistsscreen.ArtistsScreen
import com.omar.musica.artists.viewmodel.ArtistDetailsViewModel


const val ARTISTS_NAVIGATION_GRAPH = "artists_graph"
const val ARTISTS_ROUTE = "artists"
const val ARTIST_DETAIL_ROUTE = "artist/{${ArtistDetailsViewModel.ARTIST_ID_KEY}}"


fun NavController.navigateToArtists(navOptions: NavOptions? = null) {
    navigate(ARTISTS_NAVIGATION_GRAPH, navOptions)
}

fun NavController.navigateToArtistDetail(artistId: Int, navOptions: NavOptions? = null) {
    navigate("artist/$artistId", navOptions)
}

fun NavGraphBuilder.artistsGraph(
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
        route = ARTISTS_NAVIGATION_GRAPH,
        startDestination = ARTISTS_ROUTE,
    ) {
        composable(
            ARTISTS_ROUTE,
            enterTransition = {
                enterAnimationFactory(ARTISTS_ROUTE, this)
            },
            exitTransition = {
                exitAnimationFactory(ARTISTS_ROUTE, this)
            },
            popEnterTransition = {
                popEnterAnimationFactory(ARTISTS_ROUTE, this)
            },
            popExitTransition = {
                popExitAnimationFactory(ARTISTS_ROUTE, this)
            }
        ) {
            ArtistsScreen(
                modifier = contentModifier.value,
                onArtistClicked = { artistId -> navController.navigateToArtistDetail(artistId) }
            )
        }

        composable(
            ARTIST_DETAIL_ROUTE,
            enterTransition = {
                enterAnimationFactory(ARTIST_DETAIL_ROUTE, this)
            },
            popExitTransition = {
                popExitAnimationFactory(ARTIST_DETAIL_ROUTE, this)
            },
            arguments = listOf(
                navArgument(ArtistDetailsViewModel.ARTIST_ID_KEY) {
                    type = NavType.IntType
                }
            )
        )
        {
            ArtistDetailsScreen(
                modifier = contentModifier.value,
                onBackClicked = { navController.popBackStack() },
                onNavigateToArtist = { artistId -> navController.navigateToArtistDetail(artistId) }
            )
        }
    }

} 