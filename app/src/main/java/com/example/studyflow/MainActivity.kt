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
                // Основной навигатор приложения
                val navController = rememberNavController()

                Scaffold { innerPadding ->
                    // NavHost — это контейнер для твоих окон из наброска
                    NavHost(
                        navController = navController,
                        startDestination = "home", // Начинаем с главного экрана
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // 1. Главный экран (с поиском и плитками)
                        composable("home") {
                            HomeScreen(
                                onOpenCalendar = { navController.navigate("calendar") },
                                onOpenDetail = { navController.navigate("detail") }
                            )
                        }

                        // 2. Экран календаря (сетка месяца)
                        composable("calendar") {
                            CalendarScreen(onBack = { navController.popBackStack() })
                        }

                        // 3. Экран деталей (редактирование текста)
                        composable("detail") {
                            DetailScreen(onBack = { navController.popBackStack() })
                        }
                    }
                }
            }
        }
    }
}

// Временные заглушки для твоих окон, чтобы проект запускался:
@Composable
fun HomeScreen(onOpenCalendar: () -> Unit, onOpenDetail: () -> Unit) {
    // Сюда мы сейчас напишем код для плиток и поиска
}

@Composable
fun CalendarScreen(onBack: () -> Unit) {
    /* Твой набросок с сеткой мая */
}

@Composable
fun DetailScreen(onBack: () -> Unit) {
    /* Твой набросок с текстом и кнопками удаления/сохранения */
}