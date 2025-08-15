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




@Database(
    entities = [StudyEvent::class],
    version = 2,

    exportSchema = false
)


abstract class EventDatabase : RoomDatabase() {
    abstract fun eventDao(): StudyEventDao


    companion object {
        @Volatile
        private var INSTANCE: EventDatabase? = null

        fun getDatabase(context: Context, composableScope: CoroutineScope): EventDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EventDatabase::class.java,
                    "event_database"
                )
                    // Wipes and rebuilds instead of migrating if no Migration object.
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Pre-populate the database after creation
                            INSTANCE?.let { database ->
                                // Launch a coroutine to call the suspend function
                                CoroutineScope(Dispatchers.IO).launch {
                                    val eventDao = database.eventDao()
                                    eventDao.insert(
                                        studyEvent = StudyEvent(
                                            title = "Test Event on Create",
                                            description = "Populated when DB was created.",
                                            date = "2025-10-12",
                                            time = "10:00 AM"

                                        )
                                    )
                                    println("DatabaseCheck: Populated when DB was created.")
                                }
                            }
                        }
                        override fun onOpen(db: SupportSQLiteDatabase) {
                            super.onOpen(db)
                            println("EventDatabase: onOpen was opened")
                            println("${Thread.currentThread().name}: onOpen was opened")
                            // Optionally, log the latest event when the database is opened
                            INSTANCE?.let { database ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    val eventDao = database.eventDao()
                                    val allEvents = eventDao.getAllEvents() // Assuming you have such a method
                                    println("Title: $allEvents")
                                }
                            }

                        }
                    })
                    .fallbackToDestructiveMigration() // Drops and recreates the database on version mismatch
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}