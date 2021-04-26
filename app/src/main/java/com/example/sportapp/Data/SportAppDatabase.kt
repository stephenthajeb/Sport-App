package com.example.sportapp.Data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.sportapp.Converter.Converters
import kotlinx.coroutines.CoroutineScope


@Database(entities = [Schedule::class, History::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class SportAppDatabase: RoomDatabase(){
    abstract fun scheduleDAO() : ScheduleDAO
    abstract fun historyDAO(): HistoryDAO

    companion object {
        @Volatile
        private var INSTANCE: SportAppDatabase? = null

        //Singleton
        fun getDatabase(context: Context, scope: CoroutineScope): SportAppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SportAppDatabase::class.java,
                    "schedule_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}