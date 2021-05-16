package com.skyletto.startappfrontend.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.skyletto.startappfrontend.domain.entities.ProjectRole
import io.reactivex.Single

@Dao
interface RoleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(role: ProjectRole): Single<Long>

    @Query("select * from roles")
    fun getAll(): LiveData<List<ProjectRole>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAll(list: List<ProjectRole>): List<Long>

    @Query("select * from roles where id=:id")
    fun getById(id: Long): Single<ProjectRole>

    @Query("delete from roles")
    fun removeAll(): Single<Int>
}