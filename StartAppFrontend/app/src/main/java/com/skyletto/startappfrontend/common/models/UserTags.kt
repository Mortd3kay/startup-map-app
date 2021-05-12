package com.skyletto.startappfrontend.common.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_tags", primaryKeys = ["userId", "tagId"])
data class UserTags(
        val userId: Long,
        val tagId: Long
)
