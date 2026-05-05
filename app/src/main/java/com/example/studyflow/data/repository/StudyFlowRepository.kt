package com.example.studyflow.data.repository

import com.example.studyflow.data.local.dao.EventDao
import com.example.studyflow.data.local.dao.NoteDao
import com.example.studyflow.data.local.entity.EventEntity
import com.example.studyflow.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StudyFlowRepository @Inject constructor(
    private val noteDao: NoteDao,
    private val eventDao: EventDao
) {

    fun getAllNotes(): Flow<List<NoteEntity>> {
        return noteDao.getAllNotes()
    }

    fun searchNotesByTitle(query: String): Flow<List<NoteEntity>> {
        return noteDao.searchNotesByTitle(query)
    }

    suspend fun getNoteById(id: Long): NoteEntity? {
        return noteDao.getNoteById(id)
    }

    suspend fun insertNote(note: NoteEntity): Long {
        return noteDao.insertNote(note)
    }

    suspend fun updateNote(note: NoteEntity) {
        noteDao.updateNote(note)
    }

    suspend fun deleteNote(note: NoteEntity) {
        noteDao.deleteNote(note)
    }


    fun getAllEvents(): Flow<List<EventEntity>> {
        return eventDao.getAllEvents()
    }

    fun searchEventsByTitle(query: String): Flow<List<EventEntity>> {
        return eventDao.searchEventsByTitle(query)
    }

    suspend fun getEventById(id: Long): EventEntity? {
        return eventDao.getEventById(id)
    }

    suspend fun insertEvent(event: EventEntity): Long {
        return eventDao.insertEvent(event)
    }

    suspend fun updateEvent(event: EventEntity) {
        eventDao.updateEvent(event)
    }

    suspend fun deleteEvent(event: EventEntity) {
        eventDao.deleteEvent(event)
    }
}