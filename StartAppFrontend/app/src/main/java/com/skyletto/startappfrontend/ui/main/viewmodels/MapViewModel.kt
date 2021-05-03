package com.skyletto.startappfrontend.ui.main.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.skyletto.startappfrontend.ui.main.ActivityFragmentChanger

class MapViewModel(application: Application) : AndroidViewModel(application){

    var search = ""
    var categoryId = 0L
    set(value) {
        field=value
        loadCategory()
    }

    var activity: ActivityFragmentChanger? = null

    fun goToSettings(){
        activity?.goToSettings()
    }

    private fun loadCategory(){
        Log.d(TAG, "loadCategory: $categoryId")
    }

    companion object{
        private const val TAG = "MAP_VIEW_MODEL"
    }
}