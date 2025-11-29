package com.aravind.parva.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.aravind.parva.data.local.dao.MahaParvaDao
import com.aravind.parva.data.local.entities.MahaParvaEntity

/**
 * Room database for Parva app
 */
@Database(
    entities = [MahaParvaEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ParvaDatabase : RoomDatabase() {
    
    abstract fun mahaParvaDao(): MahaParvaDao

    companion object {
        @Volatile
        private var INSTANCE: ParvaDatabase? = null

        fun getDatabase(context: Context): ParvaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ParvaDatabase::class.java,
                    "parva_database"
                )
                    .fallbackToDestructiveMigration() // For development
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

