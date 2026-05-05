package com.example.studyflow.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studyflow.ui.theme.*
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

enum class EventStatus { DONE, PENDING, FAILED }
data class CalendarEvent(val day: Int, val title: String, val status: EventStatus)

@Composable
fun EventCalendar(onBack: () -> Unit) {
    val today = remember { LocalDate.now() }
    val currentMonth = remember { YearMonth.now() }

    val daysInMonth = currentMonth.lengthOfMonth()
    val firstDayOfWeek = currentMonth.atDay(1).dayOfWeek.value
    val emptyCellsCount = firstDayOfWeek - 1

    val monthName = currentMonth.month.getDisplayName(TextStyle.FULL, Locale("ru", "RU")).uppercase()
    val year = currentMonth.year

    val events = listOf(
        CalendarEvent(4, "May Day", EventStatus.DONE),
        CalendarEvent(5, "Constitution Day", EventStatus.PENDING),
        CalendarEvent(18, "Databases Exam", EventStatus.FAILED),
        CalendarEvent(18, "Поездка", EventStatus.DONE),
        CalendarEvent(25, "ADS Midterm", EventStatus.PENDING),
        CalendarEvent(27, "Kurman Ait", EventStatus.DONE)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightAppBackground)
            .padding(8.dp)
    ) {
        Text(
            text = "$monthName $year",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextMainUI,
            modifier = Modifier.padding(bottom = 16.dp, start = 8.dp, top = 16.dp)
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            val daysOfWeek = listOf("ПН", "ВТ", "СР", "ЧТ", "ПТ", "СБ", "ВС")
            daysOfWeek.forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (day == "СБ" || day == "ВС") Color.Red.copy(alpha = 0.7f) else TextMainUI
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxSize()
        ) {
            items(emptyCellsCount) {
                Box(modifier = Modifier.height(90.dp))
            }

            items(daysInMonth) { index ->
                val currentDay = index + 1
                val dayEvents = events.filter { it.day == currentDay }

                val isToday = (today.year == currentMonth.year) &&
                        (today.month == currentMonth.month) &&
                        (today.dayOfMonth == currentDay)

                DayCell(day = currentDay, events = dayEvents, isToday = isToday)
            }
        }
    }
}

@Composable
fun DayCell(day: Int, events: List<CalendarEvent>, isToday: Boolean) {
    val borderModifier = if (isToday) {
        Modifier.border(2.dp, TealPrimary, RoundedCornerShape(4.dp))
    } else {
        Modifier.border(0.5.dp, Color.LightGray.copy(alpha = 0.5f))
    }

    Column(
        modifier = Modifier
            .height(90.dp)
            .background(WhiteSurface)
            .then(borderModifier)
            .padding(2.dp)
    ) {
        Text(
            text = day.toString(),
            fontSize = 12.sp,
            color = if (isToday) TealPrimary else TextUserData,
            fontWeight = if (isToday) FontWeight.ExtraBold else FontWeight.Bold,
            modifier = Modifier.padding(start = 2.dp, top = 2.dp)
        )

        Spacer(modifier = Modifier.height(2.dp))

        events.take(3).forEach { event ->
            val backgroundColor = when (event.status) {
                EventStatus.DONE -> LightStatusDone
                EventStatus.FAILED -> LightStatusFailed
                EventStatus.PENDING -> LightStatusPending
            }

            Text(
                text = event.title,
                fontSize = 9.sp,
                color = TextUserData,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 1.dp)
                    .background(backgroundColor, RoundedCornerShape(2.dp))
                    .padding(horizontal = 2.dp, vertical = 2.dp)
            )
        }

        if (events.size > 3) {
            Text(
                text = "+${events.size - 3} еще",
                fontSize = 8.sp,
                color = Color.Gray,
                modifier = Modifier.padding(start = 2.dp)
            )
        }
    }
}