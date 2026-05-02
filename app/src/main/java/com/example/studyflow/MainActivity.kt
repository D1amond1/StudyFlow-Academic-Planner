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
import com.example.studyflow.ui.theme.StudyFlowTheme

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
                        startDestination = "home",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("home") {
                            HomeScreen(
                                onOpenCalendar = { navController.navigate("calendar") },
                                onOpenDetail = { navController.navigate("detail") }
                            )
                        }

                        composable("calendar") {
                            CalendarScreen(onBack = { navController.popBackStack() })
                        }

                        composable("detail") {
                            DetailScreen(onBack = { navController.popBackStack() })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(onOpenCalendar: () -> Unit, onOpenDetail: () -> Unit) {
}

@Composable
fun CalendarScreen(onBack: () -> Unit) {
}

@Composable
fun DetailScreen(onBack: () -> Unit) {
}