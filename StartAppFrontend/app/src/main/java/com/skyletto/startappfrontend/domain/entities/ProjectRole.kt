package com.skyletto.startappfrontend.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "roles")
data class ProjectRole(
        @PrimaryKey var id: Long = 0,
        var name: String? = null
)

