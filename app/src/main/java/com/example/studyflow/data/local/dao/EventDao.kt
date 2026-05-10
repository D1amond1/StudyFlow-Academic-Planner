package com.example.studyflow.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.studyflow.data.local.entity.EventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Query("SELECT * FROM events ORDER BY deadlineTime ASC")
    fun getAllEvents(): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE title LIKE '%' || :searchQuery || '%' ORDER BY deadlineTime ASC")
    fun searchEventsByTitle(searchQuery: String): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE id = :eventId LIMIT 1")
    fun getEventById(eventId: Long): Flow<EventEntity?>   // ← изменил на Flow

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventEntity): Long

    @Update
    suspend fun updateEvent(event: EventEntity)

    @Delete
    suspend fun deleteEvent(event: EventEntity)
}