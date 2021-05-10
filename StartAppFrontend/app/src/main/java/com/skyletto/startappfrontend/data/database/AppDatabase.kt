package com.skyletto.startappfrontend.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.skyletto.startappfrontend.common.models.UserTags
import com.skyletto.startappfrontend.data.database.dao.*
import com.skyletto.startappfrontend.domain.entities.Chat
import com.skyletto.startappfrontend.domain.entities.Message
import com.skyletto.startappfrontend.domain.entities.Tag
import com.skyletto.startappfrontend.domain.entities.User

@Database(entities = [Tag::class, Message::class, Chat::class, User::class, UserTags::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun tagDao(): TagDao
    abstract fun messageDao(): MessageDao
    abstract fun chatDao(): ChatDao
    abstract fun userDao(): UserDao
    abstract fun userTagsDao(): UserTagsDao

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