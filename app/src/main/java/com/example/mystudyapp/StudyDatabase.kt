package com.example.mystudyapp

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase

@Database(
    entities = [StudyEvent::class],
    version = 1,
    exportSchema = false
)
abstract class StudyDatabase : RoomDatabase() {
    abstract fun studyEventDao(): StudyEventDao
}

@Entity(tableName = "study_db") // Example table name
data class StudyEvent (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String
    // Add other columns
)

@Dao
interface StudyEventDao {
    @Insert
    suspend fun insert(entity: StudyEvent)

//    @Query("SELECT * FROM study_db")
//    fun getAll(): List<StudyEvent>
@Query("SELECT * FROM study_db")
fun getAllFlow(): kotlinx.coroutines.flow.Flow<List<StudyEvent>>
}