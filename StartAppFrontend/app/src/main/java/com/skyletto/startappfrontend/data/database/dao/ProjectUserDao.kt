package com.skyletto.startappfrontend.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.skyletto.startappfrontend.common.models.ProjectTags
import com.skyletto.startappfrontend.common.models.ProjectUser
import com.skyletto.startappfrontend.common.models.UserTags
import com.skyletto.startappfrontend.domain.entities.Project
import io.reactivex.Single

@Dao
interface ProjectUserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(ut: ProjectUser)

    @Query("select * from project_user where userId = :id")
    fun getByUserId(id:Long): LiveData<List<ProjectUser>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAll(list: List<ProjectUser>)

    @Query("delete from project_user")
    fun removeAll()
}