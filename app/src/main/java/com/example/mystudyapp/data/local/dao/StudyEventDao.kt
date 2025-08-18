package com.example.mystudyapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mystudyapp.data.local.entity.StudyEvent
import kotlinx.coroutines.flow.Flow


@Dao
interface StudyEventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudyEvent(studyEvent: StudyEvent)


    // --- New method to get the latest event ---
    @Query("SELECT * FROM study_events ORDER BY id DESC LIMIT 1") // Order by ID descending, take only 1
    fun getLatestEvent(): Flow<StudyEvent?> // Returns a Flow of a single nullable StudyEvent

    @Insert
    suspend fun insert(studyEvent: StudyEvent)

    @Query("SELECT COUNT(*) FROM study_events") // Replace your_table_name
    suspend fun getCount(): Int
    suspend fun isStudyTableEmpty(): Boolean {
        return (getCount() == 0)
    }

    @Query("SELECT * FROM study_events")
    suspend fun getAllEvents(): List<StudyEvent>

    @Query("SELECT * FROM study_events ORDER BY id DESC") // Example: Order by ID descending
    fun getAllStudyEvents(): Flow<List<StudyEvent>>


    companion object
}