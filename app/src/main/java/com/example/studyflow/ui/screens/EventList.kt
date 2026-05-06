package com.example.studyflow.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.studyflow.data.local.entity.EventEntity
import com.example.studyflow.ui.navigation.Screen
import com.example.studyflow.ui.theme.*
import com.example.studyflow.ui.viewmodel.EventsViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventListScreen(
    navController: NavController,
    viewModel: EventsViewModel = hiltViewModel()
) {
    val events by viewModel.events.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.width(280.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("StudyFlow", fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 16.dp))
                    DrawerItem(title = "Notes", isSelected = false) {
                        navController.navigate(Screen.Notes.route)
                    }
                    DrawerItem(title = "Events", isSelected = true) {
                        coroutineScope.launch { drawerState.close() }
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("StudyFlow", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = null)
                        }
                    },
                    actions = {
                        IconButton(onClick = { navController.navigate(Screen.EventCalendar.route) }) {
                            Icon(Icons.Default.DateRange, contentDescription = "Calendar View")
                        }
                        IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                            Icon(Icons.Default.Settings, contentDescription = "Settings")
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate(Screen.NewEvent.route) },
                    containerColor = TealPrimary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Event", tint = Color.White)
                }
            }
        ) { padding ->
            Column(modifier = Modifier.padding(padding).padding(horizontal = 16.dp)) {
                TextField(
                    value = searchQuery,
                    onValueChange = { viewModel.updateSearchQuery(it) },
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)),
                    placeholder = { Text("Search events...") },
                    trailingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(events) { event ->
                        EventItem(
                            event = event,
                            onClick = { navController.navigate(Screen.EventEdit.createRoute(event.id)) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EventItem(event: EventEntity, onClick: () -> Unit) {
    val statusColor = when (event.status) {
        1 -> LightStatusDone
        2 -> LightStatusFailed
        else -> LightStatusPending
    }

    val statusIcon = when (event.status) {
        1 -> Icons.Default.CheckCircle
        2 -> Icons.Default.Close
        else -> Icons.Default.Info
    }

    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(statusColor)
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(event.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(formatDeadline(event.deadlineTime), fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(statusIcon, contentDescription = null, modifier = Modifier.size(16.dp))
                }
            }
            Text(
                text = event.description,
                modifier = Modifier.padding(12.dp),
                fontSize = 13.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

fun formatDeadline(time: Long): String {
    val sdf = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault())
    return sdf.format(Date(time))
}