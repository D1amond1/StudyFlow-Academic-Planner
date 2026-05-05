package com.example.studyflow.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studyflow.data.local.entity.EventEntity
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
class EventsViewModel @Inject constructor(
    private val repository: StudyFlowRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    @OptIn(ExperimentalCoroutinesApi::class)
    val events: StateFlow<List<EventEntity>> = _searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) {
                repository.getAllEvents()
            } else {
                repository.searchEventsByTitle(query)
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

    fun saveNewEvent(
        title: String,
        description: String,
        deadlineTime: Long,
        status: Int = 0,
        category: String = "General"
    ) {
        viewModelScope.launch {
            if (title.isNotBlank() || description.isNotBlank()) {
                val newEvent = EventEntity(
                    title = title.ifBlank { "Event..." },
                    description = description,
                    deadlineTime = deadlineTime,
                    status = status,
                    category = category
                )
                repository.insertEvent(newEvent)
            }
        }
    }

    fun updateEvent(
        event: EventEntity,
        newTitle: String,
        newDescription: String,
        newDeadlineTime: Long,
        newStatus: Int,
        newCategory: String
    ) {
        viewModelScope.launch {
            val updatedEvent = event.copy(
                title = newTitle.ifBlank { "Event..." },
                description = newDescription,
                deadlineTime = newDeadlineTime,
                status = newStatus,
                category = newCategory
            )
            repository.updateEvent(updatedEvent)
        }
    }

    fun updateEventStatus(event: EventEntity, newStatus: Int) {
        viewModelScope.launch {
            repository.updateEvent(event.copy(status = newStatus))
        }
    }

    fun deleteEvent(event: EventEntity) {
        viewModelScope.launch {
            repository.deleteEvent(event)
        }
    }
}