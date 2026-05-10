package com.example.studyflow.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.studyflow.ui.theme.*
import com.example.studyflow.ui.viewmodel.EventsViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewEventScreen(
    navController: NavController,
    viewModel: EventsViewModel = hiltViewModel()
) {
    val events by viewModel.events.collectAsState()

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var status by remember { mutableStateOf(0) }

    val calendar = remember { Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) } }
    var deadlineTime by remember { mutableStateOf(calendar.timeInMillis) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val headerColor = when (status) {
        1 -> LightStatusDone
        2 -> LightStatusFailed
        else -> LightStatusPending
    }

    val defaultTitle = "Event ${events.size + 1}"

    val onSave = {
        if (title.isNotBlank() || description.isNotBlank()) {
            viewModel.saveNewEvent(
                title = title.ifBlank { defaultTitle },
                description = description,
                deadlineTime = deadlineTime,
                status = status
            )
        }
        navController.popBackStack()
    }

    BackHandler { onSave() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    BasicTextField(
                        value = title,
                        onValueChange = { title = it },
                        textStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black),
                        decorationBox = { innerTextField ->
                            if (title.isEmpty()) Text("Event...", color = Color.DarkGray, fontSize = 20.sp)
                            innerTextField()
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onSave() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.Black)
                    }
                },
                actions = {
                    StatusSelector(
                        currentStatus = status,
                        onStatusSelected = { status = it }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = headerColor)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onSave() },
                containerColor = TealPrimary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Check, contentDescription = "Save")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(onClick = { showDatePicker = true }) {
                    Text("Date: ${SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(deadlineTime))}")
                }
                OutlinedButton(onClick = { showTimePicker = true }) {
                    Text("Time: ${SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(deadlineTime))}")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            BasicTextField(
                value = description,
                onValueChange = { description = it },
                modifier = Modifier.fillMaxSize(),
                textStyle = TextStyle(fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface),
                decorationBox = { innerTextField ->
                    if (description.isEmpty()) Text("New event description...", color = Color.Gray)
                    innerTextField()
                }
            )
        }
    }


    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = deadlineTime)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        val timeCal = Calendar.getInstance().apply { timeInMillis = deadlineTime }
                        val newCal = Calendar.getInstance().apply { timeInMillis = it }
                        newCal.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY))
                        newCal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE))
                        deadlineTime = newCal.timeInMillis
                    }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePicker) {
        val timeCal = Calendar.getInstance().apply { timeInMillis = deadlineTime }
        val timePickerState = rememberTimePickerState(
            initialHour = timeCal.get(Calendar.HOUR_OF_DAY),
            initialMinute = timeCal.get(Calendar.MINUTE)
        )
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val newCal = Calendar.getInstance().apply { timeInMillis = deadlineTime }
                    newCal.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                    newCal.set(Calendar.MINUTE, timePickerState.minute)
                    deadlineTime = newCal.timeInMillis
                    showTimePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) { Text("Cancel") }
            },
            text = { TimePicker(state = timePickerState) }
        )
    }
}

@Composable
fun StatusSelector(
    currentStatus: Int,
    onStatusSelected: (Int) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(end = 8.dp)) {
        // Pending
        StatusIcon(icon = Icons.Default.Info, isSelected = currentStatus == 0, onClick = { onStatusSelected(0) })
        Spacer(modifier = Modifier.width(8.dp))
        // Done
        StatusIcon(icon = Icons.Default.CheckCircle, isSelected = currentStatus == 1, onClick = { onStatusSelected(1) })
        Spacer(modifier = Modifier.width(8.dp))
        // Failed
        StatusIcon(icon = Icons.Default.Close, isSelected = currentStatus == 2, onClick = { onStatusSelected(2) })
    }
}

@Composable
fun StatusIcon(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.3f))
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) Color.Black else Color.Transparent,
                shape = CircleShape
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = Color.Black, modifier = Modifier.size(20.dp))
    }
}