package com.example.studyflow.ui.navigation

sealed class Screen(val route: String) {
    object Notes : Screen("notes")
    object EventList : Screen("event_list")
    object EventCalendar : Screen("event_calendar")

    object NewNote : Screen("new_note")
    object NoteEdit : Screen("note_edit/{noteId}") {
        fun createRoute(noteId: Long) = "note_edit/$noteId"
    }

    object NewEvent : Screen("new_event")
    object EventEdit : Screen("event_edit/{eventId}") {
        fun createRoute(eventId: Long) = "event_edit/$eventId"
    }

    object Settings : Screen("settings")
}