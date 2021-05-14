package com.skyletto.startappfrontend.ui.main

interface ActivityFragmentWorker {
    fun goToProfile()
    fun goToMessages()
    fun goToMap()
    fun goToSettings()
    fun goToCreateProject()
    fun getToken(): String
    fun getUserId(): Long
}