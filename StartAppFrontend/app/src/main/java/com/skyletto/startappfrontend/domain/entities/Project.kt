package com.skyletto.startappfrontend.domain.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class Project (
        @PrimaryKey
        @ColumnInfo(name = "pId")
        var id: Long = 0,
        var title: String,
        var description: String,
        @Ignore var tags: Set<Tag>? = null,
        @Ignore var roles: List<ProjectAndRole>? = null,
        @Ignore var user: User? = null,
        var lat: Double? = null,
        var lng: Double? = null,
        var address: String? = null
) {
    constructor(id: Long, title: String, description: String):this(id, title, description, null, null, null)

    constructor(title: String, description: String, tags: Set<Tag>?, roles: List<ProjectAndRole>?) : this(0,title, description, tags, roles, null)

    constructor(title: String, description: String, tags: Set<Tag>?, roles: List<ProjectAndRole>?, user: User?):this(0, title, description, tags, roles, user)

}
