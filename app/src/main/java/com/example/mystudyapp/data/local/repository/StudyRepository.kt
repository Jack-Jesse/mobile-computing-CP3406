package com.example.mystudyapp.data.local.repository

//import androidx.compose.foundation.text2.input.insert
import com.example.mystudyapp.data.local.dao.StudyEventDao
import com.example.mystudyapp.data.local.entity.StudyEvent

class StudyRepository(private val studyEventDao: StudyEventDao) {

    suspend fun createEvent(event: StudyEvent): Result<Unit> {
        return try {
            val id = studyEventDao.insertStudyEvent(event) // Called on the injected DAO instance
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllEvents(): Result<List<StudyEvent>> {
        return try {
            val events = studyEventDao.getAllEvents() // Called on the injected DAO instance
            Result.success(events)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
