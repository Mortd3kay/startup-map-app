package com.skyletto.startappfrontend.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.skyletto.startappfrontend.common.models.UserTags
import com.skyletto.startappfrontend.data.database.dao.*
import com.skyletto.startappfrontend.domain.entities.*

@Database(entities = [Tag::class, Message::class, Chat::class, User::class, UserTags::class, ProjectRole::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun tagDao(): TagDao
    abstract fun messageDao(): MessageDao
    abstract fun chatDao(): ChatDao
    abstract fun userDao(): UserDao
    abstract fun userTagsDao(): UserTagsDao
    abstract fun roleDao(): RoleDao

    companion object {
        private var db: AppDatabase? = null
        private const val DB_NAME = "main.db"
        @JvmStatic
        fun getInstance(context: Context): AppDatabase {
            if (db == null)
                synchronized(AppDatabase::class.java) {
                if (db == null)
                    db = Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME).fallbackToDestructiveMigration().build()
            }
            return db!!
        }
    }
}