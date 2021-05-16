package com.skyletto.startappfrontend.domain.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "roles")
data class Role(
        @PrimaryKey
        @ColumnInfo(name="rId")
        var id: Long = 0,
        var name: String? = null
){
    override fun toString(): String {
        return name?:"null"
    }
}

