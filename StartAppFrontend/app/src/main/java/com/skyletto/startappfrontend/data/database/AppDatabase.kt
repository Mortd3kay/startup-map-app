package com.skyletto.startappfrontend.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.skyletto.startappfrontend.data.database.dao.TagDao
import com.skyletto.startappfrontend.domain.entities.Tag

@Database(entities = [Tag::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun tagDao(): TagDao

    companion object {
        private var db: AppDatabase? = null
        private const val DB_NAME = "main.db"
        @JvmStatic
        fun getInstance(context: Context): AppDatabase? {
            if (db == null)
                synchronized(AppDatabase::class.java) {
                if (db == null)
                    db = Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME).build()
            }
            return db
        }
    }
}