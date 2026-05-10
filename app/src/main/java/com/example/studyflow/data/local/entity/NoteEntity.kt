package com.example.studyflow.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    var title: String = "",
    var content: String = "",

    var createdAt: Long = System.currentTimeMillis(),
    var lastUpdated: Long = System.currentTimeMillis(),

    var isPinned: Boolean = false
)