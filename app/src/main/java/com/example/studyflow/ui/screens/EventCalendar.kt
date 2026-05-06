package com.example.studyflow.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventCalendarScreen(
    navController: NavController,
    viewModel: EventsViewModel = hiltViewModel()
) {
    val events by viewModel.events.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    val initialPage = 500
    val pagerState = rememberPagerState(initialPage = initialPage, pageCount = { 1000 })

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.width(280.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("StudyFlow", fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 16.dp))
                    DrawerItem(title = "Notes", isSelected = false) {
                        navController.navigate(Screen.Notes.route) {
                            popUpTo(Screen.EventCalendar.route) { inclusive = true }
                        }
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
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            navController.navigate(Screen.EventList.route) {
                                popUpTo(Screen.EventCalendar.route) { inclusive = true }
                            }
                        }) {
                            Icon(Icons.AutoMirrored.Filled.List, contentDescription = "List View")
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
                    containerColor = TealPrimary,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Event")
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    val monthOffset = page - initialPage
                    val currentMonth = YearMonth.now().plusMonths(monthOffset.toLong())

                    MonthCalendar(
                        yearMonth = currentMonth,
                        events = events,
                        onEventClick = { eventId ->
                            navController.navigate(Screen.EventEdit.createRoute(eventId))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MonthCalendar(
    yearMonth: YearMonth,
    events: List<EventEntity>,
    onEventClick: (Long) -> Unit
) {
    val daysInMonth = yearMonth.lengthOfMonth()
    val firstDayOfWeek = yearMonth.atDay(1).dayOfWeek.value

    val emptyDaysBefore = firstDayOfWeek - 1
    val totalCells = emptyDaysBefore + daysInMonth

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "${yearMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${yearMonth.year}",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
            daysOfWeek.forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
        ) {
            items(totalCells) { index ->
                if (index < emptyDaysBefore) {
                    Box(modifier = Modifier.aspectRatio(0.7f))
                } else {
                    val dayOfMonth = index - emptyDaysBefore + 1
                    val date = yearMonth.atDay(dayOfMonth)

                    val dayEvents = events.filter { event ->
                        val eventDate = Instant.ofEpochMilli(event.deadlineTime)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        eventDate == date
                    }

                    DayCell(
                        date = date,
                        events = dayEvents,
                        onClick = {
                            if (dayEvents.isNotEmpty()) {
                                onEventClick(dayEvents.first().id)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DayCell(
    date: LocalDate,
    events: List<EventEntity>,
    onClick: () -> Unit
) {
    val isToday = date == LocalDate.now()

    Box(
        modifier = Modifier
            .aspectRatio(0.7f)
            .padding(2.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isToday) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent)
            .border(
                width = if (isToday) 1.dp else 0.dp,
                color = if (isToday) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(enabled = events.isNotEmpty()) { onClick() }
            .padding(4.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = date.dayOfMonth.toString(),
                fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                color = if (isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(4.dp))

            events.take(3).forEach { event ->
                val statusColor = when (event.status) {
                    1 -> LightStatusDone
                    2 -> LightStatusFailed
                    else -> LightStatusPending
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 2.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(statusColor)
                        .padding(horizontal = 2.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = event.title,
                        fontSize = 8.sp,
                        color = Color.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            if (events.size > 3) {
                Text(
                    text = "...",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}