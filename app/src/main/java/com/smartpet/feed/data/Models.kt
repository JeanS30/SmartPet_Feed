package com.smartpet.feed.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedules")
data class Schedule(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val hour: Int,
    val minute: Int,
    val grams: Int,
    val enabled: Boolean = true
)

@Entity(tableName = "feeding_logs")
data class FeedingLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long,
    val grams: Int,
    val success: Boolean,
    val source: String          // "manual" u "horario"
)