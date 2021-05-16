package com.skyletto.startappfrontend.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.skyletto.startappfrontend.domain.entities.ProjectAndRole
import io.reactivex.Single

@Dao
interface ProjectRolesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(projectAndRole: ProjectAndRole)

    @Query("select * from projects_roles where prId in (:ids)")
    fun getAllByIds(ids: List<Long>): LiveData<List<ProjectAndRole>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAll(list: List<ProjectAndRole>): List<Long>

    @Query("select * from projects_roles where prId=:id")
    fun getById(id: Long): LiveData<ProjectAndRole>

    @Query("delete from projects_roles")
    fun removeAll(): Single<Int>
}