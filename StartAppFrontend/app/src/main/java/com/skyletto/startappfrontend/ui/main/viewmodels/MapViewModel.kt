package com.skyletto.startappfrontend.ui.main.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class MapViewModel(application: Application) : AndroidViewModel(application){
    val categories = arrayListOf("All", "Specs", "Projects")
}