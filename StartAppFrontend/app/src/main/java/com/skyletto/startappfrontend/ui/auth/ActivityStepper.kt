package com.skyletto.startappfrontend.ui.auth

import android.os.Bundle

interface ActivityStepper {
    fun nextStep()
    fun prevStep()
    fun onFinish(bundle: Bundle?)
}