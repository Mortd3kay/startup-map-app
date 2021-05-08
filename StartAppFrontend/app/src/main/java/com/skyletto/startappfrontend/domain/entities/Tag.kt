package com.skyletto.startappfrontend.domain.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.squareup.moshi.Json

@Entity(tableName = "tags")
data class Tag(
        @PrimaryKey val id: Long,
        val name: String)
