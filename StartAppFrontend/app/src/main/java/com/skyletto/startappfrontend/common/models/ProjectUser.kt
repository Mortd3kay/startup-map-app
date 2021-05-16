package com.skyletto.startappfrontend.common.models

import androidx.room.Entity

@Entity(tableName = "project_user", primaryKeys = ["projectId", "userId"])
data class ProjectUser(
        val projectId: Long,
        val userId: Long
)
