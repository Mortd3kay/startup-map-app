package com.skyletto.startappfrontend.ui.auth.viewmodels

import com.skyletto.startappfrontend.data.responses.ProfileResponse

interface OnSuccessLoginListener {
    fun onSuccess(pr: ProfileResponse)
}