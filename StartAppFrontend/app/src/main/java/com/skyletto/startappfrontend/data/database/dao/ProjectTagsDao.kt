package com.skyletto.startappfrontend.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.skyletto.startappfrontend.common.models.ProjectTags
import com.skyletto.startappfrontend.common.models.UserTags
import com.skyletto.startappfrontend.domain.entities.Project
import io.reactivex.Single

@Dao
interface ProjectTagsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(ut: ProjectTags)

    @Query("select * from project_tags where projectId = :id")
    fun getAllByProjectId(id:Long): LiveData<List<ProjectTags>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAll(list: List<ProjectTags>)

    @Query("delete from project_tags")
    fun removeAll()
}