package com.skyletto.startappfrontend.domain.entities;

import androidx.room.*

@Entity(tableName = "users")
data class User(
        @PrimaryKey
        @ColumnInfo(name = "uId")
        var id: Long? = null,
        var email: String,
        @ColumnInfo(name = "first_name") var firstName: String,
        @ColumnInfo(name = "second_name") var secondName: String,
        @ColumnInfo(name = "phone") var phoneNumber: String? = null,
        @ColumnInfo(name = "title") var title: String? = null,
        @ColumnInfo(name = "experience") var experience: String? = null,
        @ColumnInfo(name = "desc") var description: String? = null,
        @Ignore var tags: Set<Tag>? = null
){
    constructor():this(email = "", firstName = "", secondName = "")
}

