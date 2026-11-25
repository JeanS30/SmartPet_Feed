package com.smartpet.feed.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FeederDao {
    @Query("SELECT * FROM schedules ORDER BY hour, minute")
    fun schedules(): Flow<List<Schedule>>

    @Insert suspend fun insert(s: Schedule): Long
    @Update suspend fun update(s: Schedule)
    @Delete suspend fun delete(s: Schedule)

    @Query("SELECT * FROM feeding_logs ORDER BY timestamp DESC")
    fun logs(): Flow<List<FeedingLog>>

    @Insert suspend fun insertLog(l: FeedingLog)
}
