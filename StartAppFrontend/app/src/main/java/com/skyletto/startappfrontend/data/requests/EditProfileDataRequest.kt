package com.skyletto.startappfrontend.data.requests;

import com.skyletto.startappfrontend.domain.entities.Tag

data class EditProfileDataRequest(
        val id: Long,
        var email: String,
        var password: String,
        var oldPassword: String,
        var firstName: String,
        var secondName: String,
        var phoneNumber: String,
        var title: String?,
        var experience: String?,
        var description: String?,
        var tags: Set<Tag>?
)
