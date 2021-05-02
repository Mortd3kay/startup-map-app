package com.skyletto.startappfrontend.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.skyletto.startappfrontend.domain.entities.Tag

@Dao
interface TagDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(tag: Tag): Long?

    @Query("select * from tags")
    fun getAll():List<Tag>?

    @Query("select * from tags where id=:id")
    fun getById(id: Long): Tag?
}