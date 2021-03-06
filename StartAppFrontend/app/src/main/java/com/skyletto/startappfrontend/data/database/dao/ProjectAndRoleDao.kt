package com.skyletto.startappfrontend.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.skyletto.startappfrontend.domain.entities.ProjectAndRole
import io.reactivex.Single

@Dao
interface ProjectAndRoleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addRole(projectAndRole: ProjectAndRole)

    @Update
    fun updateRole(projectAndRole: ProjectAndRole)

    @Query("select * from projects_roles where prId in (:ids)")
    fun getAllRolesByIds(ids: List<Long>): LiveData<List<ProjectAndRole>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAllRoles(list: List<ProjectAndRole>): List<Long>

    @Query("select * from projects_roles where prId=:id")
    fun getRoleById(id: Long): LiveData<ProjectAndRole>

    @Query("delete from projects_roles")
    fun removeAllRoles(): Single<Int>
}