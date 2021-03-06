package com.skyletto.startappfrontend.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.skyletto.startappfrontend.domain.entities.Tag
import io.reactivex.Single

@Dao
interface TagDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(tag: Tag): Single<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addAll(tags: Iterable<Tag>)

    @Query("select * from tags")
    fun getAll(): LiveData<List<Tag>>

    @Query("select * from tags where tId=:id")
    fun getById(id: Long): Single<Tag>

    @Query("delete from tags")
    fun removeAll():Single<Int>
}