package com.example.studyflow.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studyflow.ui.theme.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewEvent(
    onBack: () -> Unit,
    onSave: (title: String, description: String, status: EventStatus, date: LocalDate?, time: LocalTime?) -> Unit = { _, _, _, _, _ -> }
) {
    val context = LocalContext.current

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var status by remember { mutableStateOf(EventStatus.PENDING) }

    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedTime by remember { mutableStateOf<LocalTime?>(null) }

    val headerBackgroundColor = when (status) {
        EventStatus.DONE -> LightStatusDone
        EventStatus.FAILED -> LightStatusFailed
        EventStatus.PENDING -> LightStatusPending
    }

    fun getIconHighlight(targetStatus: EventStatus): Color {
        return if (status == targetStatus) {
            when (targetStatus) {
                EventStatus.DONE -> LightStatusDoneSelected
                EventStatus.FAILED -> LightStatusFailedSelected
                EventStatus.PENDING -> LightStatusPendingSelected
            }
        } else Color.Transparent
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onSave(title, description, status, selectedDate, selectedTime)
                    onBack()
                },
                containerColor = TealPrimary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Check, contentDescription = "Сохранить")
            }
        },
        containerColor = LightAppBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(headerBackgroundColor)
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    onSave(title, description, status, selectedDate, selectedTime)
                    onBack()
                }) {
                    Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Назад", tint = TextUserData)
                }

                BasicTextField(
                    value = title,
                    onValueChange = { title = it },
                    textStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextUserData),
                    modifier = Modifier.weight(1f),
                    decorationBox = { innerTextField ->
                        if (title.isEmpty()) {
                            Text("Event...", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextUserData.copy(alpha = 0.5f))
                        }
                        innerTextField()
                    }
                )

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(
                        onClick = { status = EventStatus.DONE },
                        modifier = Modifier.background(getIconHighlight(EventStatus.DONE), CircleShape)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = "Сделано", tint = TextUserData)
                    }
                    IconButton(
                        onClick = { status = EventStatus.FAILED },
                        modifier = Modifier.background(getIconHighlight(EventStatus.FAILED), CircleShape)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Провалено", tint = TextUserData)
                    }
                    IconButton(
                        onClick = { status = EventStatus.PENDING },
                        modifier = Modifier.background(getIconHighlight(EventStatus.PENDING), CircleShape)
                    ) {
                        Icon(Icons.Outlined.Timer, contentDescription = "В ожидании", tint = TextUserData)
                    }
                    IconButton(onClick = { /* Логика удаления будет позже */ }) {
                        Icon(Icons.Default.Delete, contentDescription = "Удалить", tint = TextUserData)
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(WhiteSurface)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (selectedDate != null) TealPrimary.copy(alpha = 0.2f) else Color.LightGray.copy(alpha = 0.3f))
                        .clickable {
                            val now = LocalDate.now()
                            DatePickerDialog(
                                context,
                                { _, year, month, dayOfMonth ->
                                    selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                                },
                                selectedDate?.year ?: now.year,
                                (selectedDate?.monthValue ?: now.monthValue) - 1,
                                selectedDate?.dayOfMonth ?: now.dayOfMonth
                            ).show()
                        }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.DateRange, contentDescription = null, modifier = Modifier.size(16.dp), tint = TextUserData)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = selectedDate?.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) ?: "Дата",
                        fontSize = 14.sp,
                        color = TextUserData
                    )
                }

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (selectedTime != null) TealPrimary.copy(alpha = 0.2f) else Color.LightGray.copy(alpha = 0.3f))
                        .clickable {
                            val now = LocalTime.now()
                            TimePickerDialog(
                                context,
                                { _, hourOfDay, minute ->
                                    selectedTime = LocalTime.of(hourOfDay, minute)
                                },
                                selectedTime?.hour ?: now.hour,
                                selectedTime?.minute ?: now.minute,
                                true
                            ).show()
                        }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Outlined.Timer, contentDescription = null, modifier = Modifier.size(16.dp), tint = TextUserData)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = selectedTime?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: "Время",
                        fontSize = 14.sp,
                        color = TextUserData
                    )
                }
            }

            BasicTextField(
                value = description,
                onValueChange = { description = it },
                textStyle = TextStyle(fontSize = 16.sp, color = TextUserData, lineHeight = 24.sp),
                modifier = Modifier
                    .fillMaxSize()
                    .background(WhiteSurface)
                    .padding(16.dp),
                decorationBox = { innerTextField ->
                    if (description.isEmpty()) {
                        Text("New event...", fontSize = 16.sp, color = Color.Gray)
                    }
                    innerTextField()
                }
            )
        }
    }
}