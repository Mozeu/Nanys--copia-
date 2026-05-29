package com.nanys.care.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nanys.care.core.util.ColorUtil
import com.nanys.care.domain.model.Booking
import com.nanys.care.domain.model.BookingStatus
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun VisualCalendarSection(
    bookings: List<Booking>,
    modifier: Modifier = Modifier
) {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    val acceptedBookings = bookings.filter { it.status == BookingStatus.ACCEPTED || it.status == BookingStatus.COMPLETED }
    val bookingsByDate = acceptedBookings.groupBy { LocalDate.parse(it.date) }

    Column(modifier) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
                Icon(Icons.Default.ChevronLeft, "Mes anterior")
            }
            Text(
                "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale("es"))} ${currentMonth.year}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
                Icon(Icons.Default.ChevronRight, "Mes siguiente")
            }
        }

        val daysOfWeek = listOf("L", "M", "X", "J", "V", "S", "D")
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            daysOfWeek.forEach { Text(it, modifier = Modifier.weight(1f), textAlign = TextAlign.Center, style = MaterialTheme.typography.labelSmall) }
        }

        val firstDay = currentMonth.atDay(1)
        val daysInMonth = currentMonth.lengthOfMonth()
        val startOffset = (firstDay.dayOfWeek.value - 1).coerceAtLeast(0)

        val totalCells = ((startOffset + daysInMonth + 6) / 7) * 7
        Column {
            for (week in 0 until totalCells / 7) {
                Row(Modifier.fillMaxWidth()) {
                    for (day in 0 until 7) {
                        val cellIndex = week * 7 + day
                        val dayNumber = cellIndex - startOffset + 1
                        if (dayNumber in 1..daysInMonth) {
                            val date = currentMonth.atDay(dayNumber)
                            val dayBookings = bookingsByDate[date].orEmpty()
                            val isSelected = date == selectedDate
                            Box(
                                Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .padding(2.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (isSelected) MaterialTheme.colorScheme.primaryContainer
                                        else Color.Transparent
                                    )
                                    .border(
                                        if (isSelected) 1.dp else 0.dp,
                                        MaterialTheme.colorScheme.primary,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { selectedDate = date },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("$dayNumber", style = MaterialTheme.typography.bodySmall)
                                    Row(horizontalArrangement = Arrangement.Center) {
                                        dayBookings.take(3).forEach { b ->
                                            Box(
                                                Modifier
                                                    .size(6.dp)
                                                    .clip(CircleShape)
                                                    .background(ColorUtil.parseHex(b.colorHex))
                                            )
                                            Spacer(Modifier.width(2.dp))
                                        }
                                    }
                                }
                            }
                        } else {
                            Spacer(Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        Text("Citas del ${selectedDate.dayOfMonth}/${selectedDate.monthValue}/${selectedDate.year}", fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))
        val dayList = bookingsByDate[selectedDate].orEmpty()
        if (dayList.isEmpty()) {
            Text("Sin citas este día", color = MaterialTheme.colorScheme.onSurface.copy(0.5f))
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                dayList.forEach { booking ->
                    BookingCard(booking)
                }
            }
        }
    }
}
