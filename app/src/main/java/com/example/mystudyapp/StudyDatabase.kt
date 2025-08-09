package com.example.mystudyapp.data

import android.content.Context
import androidx.room.*

@Entity(tableName = "study_events")
data class StudyEvent(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val date: Long,      // epoch millis
    val time: String     // e.g. "14:30"
)

@Dao
interface StudyEventDao {
    @Insert
    suspend fun insert(entity: StudyEvent)

    @Query("SELECT * FROM study_events ORDER BY date ASC")
    fun getAllFlow(): kotlinx.coroutines.flow.Flow<List<StudyEvent>>
}

@Database(
    entities = [StudyEvent::class],
    version = 1,
    exportSchema = false
)
abstract class StudyDatabase : RoomDatabase() {
    abstract fun studyEventDao(): StudyEventDao

    companion object {
        @Volatile private var INSTANCE: StudyDatabase? = null

        fun getInstance(context: Context): StudyDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    StudyDatabase::class.java,
                    "study_db"
                ).build().also { INSTANCE = it }
            }
    }
}
