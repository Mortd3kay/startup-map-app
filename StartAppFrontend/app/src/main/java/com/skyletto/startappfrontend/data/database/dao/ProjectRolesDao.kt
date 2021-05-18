package com.skyletto.startappfrontend.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.skyletto.startappfrontend.common.models.ProjectRoles
import io.reactivex.Single

@Dao
interface ProjectRolesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(projectRoles: ProjectRoles)

    @Query("select * from project_roles where projectId in (:ids)")
    fun getAllByProjectIds(ids: List<Long>): LiveData<List<ProjectRoles>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAll(list: List<ProjectRoles>): List<Long>

    @Query("select * from project_roles where projectId=:id")
    fun getByProjectId(id: Long): LiveData<ProjectRoles>

    @Query("delete from project_roles")
    fun removeAll(): Single<Int>
}
