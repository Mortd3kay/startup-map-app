package com.skyletto.startappfrontend.common.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import com.skyletto.startappfrontend.common.utils.toast


class LocationReceiver : BroadcastReceiver() {

    var listeners = mutableListOf<OnStateReceiverChangeListener>()
    var enabled = false

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null || intent.extras == null)
            return
        context?.let {
            if (intent.action == LocationManager.PROVIDERS_CHANGED_ACTION){
                val lm = it.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                enabled = lm.isLocationEnabled
            }
        }
        for (l in listeners){
            l.notifyState(enabled)
        }
    }

}