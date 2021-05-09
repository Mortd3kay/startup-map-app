package com.skyletto.startappfrontend.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.skyletto.startappfrontend.domain.entities.Message
import io.reactivex.Single

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(message: Message):Single<Long>

    @Query("select * from messages where senderId=:id or receiverId = :id")
    fun getAllByChatId(id: Long):LiveData<List<Message>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addAll(list: List<Message>): List<Long>

    @Query("select * from messages where id=:id")
    fun getById(id: Long): Single<Message>

    @Query("delete from messages")
    fun removeAll():Single<Int>
}