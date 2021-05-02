package com.skyletto.startappfrontend.ui.auth.viewmodels

import com.skyletto.startappfrontend.data.responses.ProfileResponse

interface OnSaveProfileListener {
    fun save(pr: ProfileResponse)
}