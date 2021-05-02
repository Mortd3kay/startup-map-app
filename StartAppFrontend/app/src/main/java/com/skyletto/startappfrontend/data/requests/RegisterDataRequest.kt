package com.skyletto.startappfrontend.data.requests;

import com.skyletto.startappfrontend.domain.entities.Tag

data class RegisterDataRequest(
        var email: String,
        var password: String,
        var firstName: String,
        var secondName: String,
        var phoneNumber: String,
        var title: String?,
        var experience: String?,
        var description: String?,
        var tags: Set<Tag>?
){
    constructor(
            email: String,
            password: String,
            firstName: String,
            secondName: String,
            phoneNumber: String
    ): this(email, password, firstName, secondName, phoneNumber, null, null, null, null)
}
