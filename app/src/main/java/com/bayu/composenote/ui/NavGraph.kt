package com.bayu.composenote.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bayu.composenote.ui.addnote.AddNoteScreen
import com.bayu.composenote.ui.addnote.AddNoteViewModel
import com.bayu.composenote.ui.home.HomeScreen
import com.bayu.composenote.ui.home.HomeViewModel

sealed class Destination(val route: String)
object HomeDestination : Destination(route = "home")
object AddNoteDestination : Destination(route = "addnote?id={id}")

@Composable
fun AppGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = HomeDestination.route
    ) {
        composable(route = HomeDestination.route) {
            val viewModel = hiltViewModel<HomeViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            HomeScreen(
                uiState = uiState,
                onNoteClicked = viewModel::onNoteClicked,
                onAddNoteBtnClicked = {
                    navController.navigateToAddNoteScreen(-1)
                },
                onDismissBottomSheet = viewModel::onDismissBottomSheet,
                onEditNoteClicked = { note ->
                    navController.navigateToAddNoteScreen(note.id)
                }
            )
        }
        composable(
            route = AddNoteDestination.route,
            arguments = listOf(
                navArgument(name = "id") {
                    nullable = false
                    defaultValue = 0
                    type = NavType.IntType
                }
            )
        ) {
            val viewModel = hiltViewModel<AddNoteViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            AddNoteScreen(
                onBack = {
                    navController.navigateUp()
                },
                onAddNote = viewModel::addNote,
                state = uiState
            )
        }
    }
}

fun NavController.navigateToAddNoteScreen(id: Int) {
    navigate(route = "addnote?id=$id")
}