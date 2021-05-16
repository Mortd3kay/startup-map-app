package com.skyletto.startappfrontend.common.models

import androidx.room.Entity

@Entity(tableName = "project_tags", primaryKeys = ["projectId", "tagId"])
data class ProjectTags(
        val projectId: Long,
        val tagId: Long
)
