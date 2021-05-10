package com.skyletto.startappfrontend.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.skyletto.startappfrontend.common.models.UserTags
import io.reactivex.Single

@Dao
interface UserTagsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(ut: UserTags)

    @Query("select * from user_tags where userId in (:ids)")
    fun getAllByUserIds(ids: List<Long>): LiveData<List<UserTags>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAll(list: List<UserTags>)

    @Query("select * from user_tags where userId=:userId")
    fun getById(userId: Long): Single<UserTags>

    @Query("delete from user_tags")
    fun removeAll()
}