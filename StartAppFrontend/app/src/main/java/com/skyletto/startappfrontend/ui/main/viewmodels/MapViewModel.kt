package com.skyletto.startappfrontend.ui.main.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.skyletto.startappfrontend.ui.main.ActivityFragmentWorker

class MapViewModel(application: Application) : AndroidViewModel(application){

    var search = ""
    var categoryId = 0L

    var activity: ActivityFragmentWorker? = null

    fun goToSettings(){
        activity?.goToSettings()
    }


    companion object{
        private const val TAG = "MAP_VIEW_MODEL"
    }
}