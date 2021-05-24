package com.skyletto.startappfrontend.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.skyletto.startappfrontend.common.models.UserWithTags
import com.skyletto.startappfrontend.domain.entities.User
import io.reactivex.Single

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(user: User)

    @Query("select * from users")
    fun getAll(): LiveData<List<UserWithTags>>

    @Query("select * from users where uId in (:ids)")
    fun getAllByIds(ids: List<Long>): LiveData<List<UserWithTags>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAll(list: List<User>): List<Long>

    @Query("select * from users")
    fun getAllKnown():LiveData<List<UserWithTags>>

    @Query("select * from users where uId=:id")
    fun getById(id: Long): LiveData<UserWithTags>

    @Query("delete from users")
    fun removeAll(): Single<Int>

}