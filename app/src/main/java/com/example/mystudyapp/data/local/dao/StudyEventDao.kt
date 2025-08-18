package com.example.mystudyapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mystudyapp.data.local.entity.StudyEvent


@Dao
interface StudyEventDao {
    @Insert
    suspend fun insert(studyEvent: StudyEvent)

    @Query("SELECT COUNT(*) FROM study_events") // Replace your_table_name
    suspend fun getCount(): Int
    suspend fun isStudyTableEmpty(): Boolean {
        return if (getCount() == 0) {
            true // empty
        } else {
            false // not empty
        }
    }

    @Query("SELECT * FROM study_events")
    suspend fun getAllEvents(): List<StudyEvent>

    companion object
}