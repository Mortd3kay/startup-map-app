package com.skyletto.startappfrontend.domain.entities;

import androidx.room.PrimaryKey;

data class User(
        @PrimaryKey var id: Long?,
        var email: String,
        var password: String,
        var firstName: String,
        var secondName: String,
        var phoneNumber: String,
        var title: String?,
        var experience: String?,
        var description: String?,
        var tags: Set<Tag>?
) {
    constructor(
            email: String,
            password: String,
            firstName: String,
            secondName: String,
            phoneNumber: String
    ) :this(null, email, password, firstName, secondName, phoneNumber, null,null,null,null)
}

