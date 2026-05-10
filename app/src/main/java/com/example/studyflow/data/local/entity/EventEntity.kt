package com.example.studyflow.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    var title: String = "",
    var description: String = "",

    var deadlineTime: Long = System.currentTimeMillis(),
    var status: Int = 0,
    var headerColor: String = "#008080",

    var createdAt: Long = System.currentTimeMillis(),
    var updatedAt: Long = System.currentTimeMillis(),

    var category: String = "General"
)