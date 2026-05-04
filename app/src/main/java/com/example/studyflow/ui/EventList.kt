package com.example.studyflow.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studyflow.ui.theme.*
import androidx.compose.material.icons.outlined.Timer

data class DetailedEvent(
    val id: Int,
    val title: String,
    val description: String,
    val status: EventStatus,
    val timeText: String,
    val remainingMinutes: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventList(
    events: List<DetailedEvent>,
    onMenuClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onCalendarClick: () -> Unit,
    onAddClick: () -> Unit
) {
    val sortedEvents = remember(events) {
        events.sortedBy { it.remainingMinutes }
    }

    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("StudyFlow", fontWeight = FontWeight.Bold, color = TextMainUI) },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(Icons.Default.Menu, contentDescription = "Меню", tint = TextMainUI)
                    }
                },
                actions = {
                    IconButton(onClick = onCalendarClick) {
                        Icon(Icons.Default.DateRange, contentDescription = "Календарь", tint = TextMainUI)
                    }
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Настройки", tint = TextMainUI)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = TealPrimary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Добавить")
            }
        },
        containerColor = LightAppBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                placeholder = { Text("Поиск...", color = Color.Gray) },
                trailingIcon = { Icon(Icons.Default.Search, contentDescription = "Поиск", tint = TextMainUI) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = WhiteSurface,
                    unfocusedContainerColor = WhiteSurface,
                    focusedBorderColor = TealPrimary,
                    unfocusedBorderColor = Color.Transparent
                ),
                singleLine = true
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(sortedEvents) { event ->
                    EventCard(event = event)
                }
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}

@Composable
fun EventCard(event: DetailedEvent) {
    val (headerColor, icon) = when (event.status) {
        EventStatus.DONE -> LightStatusDone to Icons.Default.Check
        EventStatus.FAILED -> LightStatusFailed to Icons.Default.Close
        EventStatus.PENDING -> LightStatusPending to Icons.Outlined.Timer
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = WhiteSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(headerColor)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = event.title,
                    fontWeight = FontWeight.Bold,
                    color = TextUserData,
                    fontSize = 14.sp
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = event.timeText,
                        fontWeight = FontWeight.Bold,
                        color = TextUserData,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Icon(
                        imageVector = icon,
                        contentDescription = "Статус",
                        tint = TextUserData,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Text(
                text = event.description,
                color = TextUserData,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                modifier = Modifier.padding(12.dp),
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}