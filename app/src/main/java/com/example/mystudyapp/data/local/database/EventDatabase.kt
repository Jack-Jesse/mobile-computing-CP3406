package com.example.mystudyapp.data.local.database

import androidx.camera.core.impl.QuirkSettingsHolder.instance
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mystudyapp.data.local.dao.StudyEventDao
import com.example.mystudyapp.data.local.entity.StudyEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.content.Context





@Database(entities = [StudyEvent::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {


    abstract fun studyEventDao(): StudyEventDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null


        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "study_event_database" // This is the name of your DB file
                )
                    .fallbackToDestructiveMigration() // Or handle migrations properly
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
