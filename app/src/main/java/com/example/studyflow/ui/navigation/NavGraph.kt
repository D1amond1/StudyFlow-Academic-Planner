package com.example.studyflow.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.studyflow.ui.screens.EventCalendarScreen
import com.example.studyflow.ui.screens.EventEditScreen
import com.example.studyflow.ui.screens.EventListScreen
import com.example.studyflow.ui.screens.NewEventScreen
import com.example.studyflow.ui.screens.NewNoteScreen
import com.example.studyflow.ui.screens.NoteEditScreen
import com.example.studyflow.ui.screens.NotesScreen
import com.example.studyflow.ui.screens.SettingsScreen

@Composable
fun StudyFlowNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Notes.route,
        modifier = modifier
    ) {
        composable(Screen.Notes.route) {
            NotesScreen(navController = navController)
        }

        composable(Screen.EventList.route) {
            EventListScreen(navController = navController)
        }

        composable(Screen.EventCalendar.route) {
            EventCalendarScreen(navController = navController)
        }

        composable(Screen.NewNote.route) {
            NewNoteScreen(navController = navController)
        }

        composable(Screen.NewEvent.route) {
            NewEventScreen(navController = navController)
        }

        composable(
            route = Screen.NoteEdit.route,
            arguments = listOf(navArgument("noteId") { type = NavType.LongType })
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getLong("noteId") ?: -1L
            NoteEditScreen(navController = navController, noteId = noteId)
        }

        composable(
            route = Screen.EventEdit.route,
            arguments = listOf(navArgument("eventId") { type = NavType.LongType })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getLong("eventId") ?: -1L
            EventEditScreen(navController = navController, eventId = eventId)
        }

        composable(Screen.Settings.route) {
            SettingsScreen(navController = navController)
        }
    }
}