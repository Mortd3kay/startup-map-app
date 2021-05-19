package com.skyletto.startappfrontend.common.models

data class UserItem(
        val id : Long,
        val fullName: String,
        val title: String
){
    override fun toString(): String {
        return fullName
    }
}