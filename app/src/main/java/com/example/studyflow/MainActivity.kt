package com.example.studyflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.studyflow.ui.*
import com.example.studyflow.ui.theme.StudyFlowTheme

sealed class Screen(val route: String) {
    object Notes : Screen("notes")
    object EventCalendar : Screen("calendar")
    object EventList : Screen("events")
    object NewNote : Screen("new_note")
    object Settings : Screen("settings")
    object Change : Screen("change")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudyFlowTheme {
                val navController = rememberNavController()

                Scaffold { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Notes.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Notes.route) {
                            Notes(
                                onAddClick = { navController.navigate(Screen.NewNote.route) },
                                onSettingsClick = { navController.navigate(Screen.Settings.route) },
                                onMenuClick = {}
                            )
                        }

                        composable(Screen.NewNote.route) {
                            NewNote(onBack = { navController.popBackStack() })
                        }

                        composable(Screen.EventCalendar.route) {
                            EventCalendar(onBack = { navController.popBackStack() })
                        }

                        composable(Screen.Settings.route) {
                            SettingsScreen(onBack = { navController.popBackStack() })
                        }

                    }
                }
            }
        }
    }
}