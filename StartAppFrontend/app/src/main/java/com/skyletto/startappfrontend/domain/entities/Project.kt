package com.skyletto.startappfrontend.domain.entities

import androidx.databinding.Bindable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//@Entity(tableName = "projects")
data class Project (
        @PrimaryKey val id: Long = 0,
        var title: String,
        var description: String,
        var tags: Set<Tag>? = null,
        var roles: List<ProjectAndRole>? = null,
        @ColumnInfo
        var user: User? = null
) {
    constructor(title: String, description: String, tags: Set<Tag>?, roles: List<ProjectAndRole>?) : this(0,title, description, tags, roles, null)

    constructor(title: String, description: String, tags: Set<Tag>?, roles: List<ProjectAndRole>?, user: User?):this(0, title, description, tags, roles, user)
}
