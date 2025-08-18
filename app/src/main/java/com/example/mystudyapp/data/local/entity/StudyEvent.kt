package com.example.mystudyapp.data.local.entity


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "study_events") // Optional: define a custom table name
data class StudyEvent(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val date: String?,
    val time: String?
)
