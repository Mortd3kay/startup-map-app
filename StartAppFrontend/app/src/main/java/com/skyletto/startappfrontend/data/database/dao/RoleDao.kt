package com.skyletto.startappfrontend.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.skyletto.startappfrontend.domain.entities.Role
import io.reactivex.Single

@Dao
interface RoleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(role: Role): Single<Long>

    @Query("select * from roles")
    fun getAll(): LiveData<List<Role>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAll(list: List<Role>): List<Long>

    @Query("select * from roles where rId=:id")
    fun getById(id: Long): Single<Role>

    @Query("delete from roles")
    fun removeAll(): Single<Int>
}