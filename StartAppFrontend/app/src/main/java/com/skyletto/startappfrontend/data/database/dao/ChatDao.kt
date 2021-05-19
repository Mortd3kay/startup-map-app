package com.skyletto.startappfrontend.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.skyletto.startappfrontend.domain.entities.Chat
import io.reactivex.Single

@Dao
interface ChatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(chat: Chat): Single<Long>

    @Query("select * from chats")
    fun getAll(): LiveData<List<Chat>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAll(list: List<Chat>): List<Long>

    @Query("select * from chats where chat_id=:id")
    fun getById(id: Long): Single<Chat>

    @Query("select chat_id from chats")
    fun getAllIds(): LiveData<List<Long>>

    @Query("delete from chats")
    fun removeAll(): Single<Int>
}