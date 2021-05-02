package com.skyletto.startappfrontend.domain.entities;

import com.squareup.moshi.Json

data class Role(@Json(name="id") val id: Int,
                @Json(name="name") val name: String)
