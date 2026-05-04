package com.example.studyflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.studyflow.ui.*
import com.example.studyflow.ui.NewNote
import com.example.studyflow.ui.theme.StudyFlowTheme
import com.example.studyflow.ui.theme.TextMainUI
import kotlinx.coroutines.launch

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
        setContent {
            StudyFlowTheme {
                val navController = rememberNavController()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet(
                            drawerContainerColor = Color(0xFFB4B4B4),
                            modifier = Modifier.width(260.dp)
                        ) {
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = "StudyFlow",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(16.dp),
                                color = TextMainUI
                            )

                            HorizontalDivider(color = Color.DarkGray)
                            Spacer(modifier = Modifier.height(16.dp))

                            NavigationDrawerItem(
                                label = { Text("Notes", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                                selected = false,
                                onClick = {
                                    navController.navigate(Screen.Notes.route)
                                    scope.launch { drawerState.close() }
                                },
                                colors = NavigationDrawerItemDefaults.colors(
                                    unselectedContainerColor = Color.Transparent,
                                    unselectedTextColor = TextMainUI
                                ),
                                modifier = Modifier.padding(horizontal = 12.dp)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            NavigationDrawerItem(
                                label = { Text("Events", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                                selected = false,
                                onClick = {
                                    navController.navigate(Screen.EventList.route)
                                    scope.launch { drawerState.close() }
                                },
                                colors = NavigationDrawerItemDefaults.colors(
                                    unselectedContainerColor = Color.Transparent,
                                    unselectedTextColor = Color.Black
                                ),
                                modifier = Modifier.padding(horizontal = 12.dp)
                            )
                        }
                    }
                ) {
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
                                    onMenuClick = { scope.launch { drawerState.open() } }
                                )
                            }

                            composable(Screen.NewNote.route) {
                                NewNote(onBack = { navController.popBackStack() })
                            }

                            composable(Screen.EventList.route) {
                                val dummyEventsList = listOf(
                                    DetailedEvent(1, "Сдать лабу по ADS", "Нужно дописать алгоритм красно-черного дерева и залить на GitHub.", EventStatus.FAILED, "2h 00m", 120),
                                    DetailedEvent(2, "Подготовка к экзамену БД", "Повторить нормальные формы и B+ деревья.", EventStatus.DONE, "2d 23h", 4300),
                                    DetailedEvent(3, "Созвон с командой", "Обсудить макеты в Figma и структуру базы данных.", EventStatus.PENDING, "1m 30s", 90)
                                )

                                EventList(
                                    events = dummyEventsList,
                                    onMenuClick = { scope.launch { drawerState.open() } },
                                    onSettingsClick = { navController.navigate(Screen.Settings.route) },

                                    onCalendarClick = { navController.navigate(Screen.EventCalendar.route) },

                                    onAddClick = { navController.navigate(Screen.NewNote.route) }
                                )
                            }

                            composable(Screen.EventCalendar.route) {
                                EventCalendar(
                                    onBack = { navController.popBackStack() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}