package com.skyletto.startappfrontend.common.services

import android.app.Service
import android.content.Intent
import android.os.IBinder

class LocationService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}