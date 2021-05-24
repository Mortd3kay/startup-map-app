package com.skyletto.startappfrontend.domain.entities

data class Location(var userId: Long, var lat: Double, var lng: Double, var isProject:Boolean = false)