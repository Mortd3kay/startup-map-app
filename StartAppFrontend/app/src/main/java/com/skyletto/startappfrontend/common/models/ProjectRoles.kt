package com.skyletto.startappfrontend.common.models

import androidx.room.Entity

@Entity(tableName = "project_roles", primaryKeys = ["projectId", "roleId"])
data class ProjectRoles(
        val projectId: Long,
        val roleId: Long
)
