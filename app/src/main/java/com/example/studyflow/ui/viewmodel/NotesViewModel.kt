package com.example.studyflow.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studyflow.data.local.entity.NoteEntity
import com.example.studyflow.data.repository.StudyFlowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val repository: StudyFlowRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    @OptIn(ExperimentalCoroutinesApi::class)
    val notes: StateFlow<List<NoteEntity>> = _searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) {
                repository.getAllNotes()
            } else {
                repository.searchNotesByTitle(query)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun saveNewNote(title: String, content: String) {
        viewModelScope.launch {
            // Если и заголовок, и текст пустые - не сохраняем мусор
            if (title.isNotBlank() || content.isNotBlank()) {
                val newNote = NoteEntity(
                    title = title.ifBlank { "New note..." },
                    content = content
                )
                repository.insertNote(newNote)
            }
        }
    }

    fun updateNote(note: NoteEntity, newTitle: String, newContent: String) {
        viewModelScope.launch {
            val updatedNote = note.copy(
                title = newTitle.ifBlank { "Note..." },
                content = newContent,
                lastUpdated = System.currentTimeMillis()
            )
            repository.updateNote(updatedNote)
        }
    }

    fun deleteNote(note: NoteEntity) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }
}