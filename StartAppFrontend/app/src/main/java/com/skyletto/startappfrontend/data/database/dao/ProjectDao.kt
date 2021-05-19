package com.skyletto.startappfrontend.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.skyletto.startappfrontend.common.models.ProjectWithTagsAndRoles
import com.skyletto.startappfrontend.common.models.UserWithTags
import com.skyletto.startappfrontend.domain.entities.Project
import com.skyletto.startappfrontend.domain.entities.User
import io.reactivex.Single

@Dao
interface ProjectDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(project: Project)

    @Query("select * from projects where pId in (:ids)")
    fun getAllByIds(ids: List<Long>): LiveData<List<ProjectWithTagsAndRoles>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAll(list: List<Project>): List<Long>

    @Query("select * from projects where pId=:id")
    fun getById(id: Long): LiveData<ProjectWithTagsAndRoles>

    @Query("select * from projects t1,project_user t2 where t2.userId=:id and t1.pId = t2.projectId")
    fun getAllByUserId(id: Long): LiveData<List<ProjectWithTagsAndRoles>>

    @Delete
    fun removeProject(project: Project)

    @Query("delete from projects")
    fun removeAll(): Single<Int>
}