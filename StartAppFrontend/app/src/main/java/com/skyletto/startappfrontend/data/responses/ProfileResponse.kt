package com.skyletto.startappfrontend.data.responses;

import com.skyletto.startappfrontend.domain.entities.User;

data class ProfileResponse(var token: String, var user: User)