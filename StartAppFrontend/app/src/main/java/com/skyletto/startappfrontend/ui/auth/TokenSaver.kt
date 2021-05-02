package com.skyletto.startappfrontend.ui.auth

interface TokenSaver {
    fun save(token: String?, id: Long)
}