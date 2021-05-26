package com.skyletto.startappfrontend.common.services

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.IBinder
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.skyletto.startappfrontend.common.MainApplication
import com.skyletto.startappfrontend.data.network.ApiRepository
import com.skyletto.startappfrontend.data.network.ApiRepository.makeToken
import com.skyletto.startappfrontend.data.requests.LatLngRequest
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class LocationService : Service(), LocationListener {
    private var api: ApiRepository? = null
    private var token: String? = null
    private val latLngRequest = MutableLiveData<LatLngRequest>()
    private var d: Disposable? = null
    private var mContext: Context? = null

    private var isGPSEnabled = false
    private var isNetworkEnabled = false
    var canGetLocation = false
    private var location: Location? = null
    protected var locationManager: LocationManager? = null


    override fun onCreate() {
        super.onCreate()
        api = (application as MainApplication).api
        mContext = applicationContext
        if (!initToken()) {
            Log.d(TAG, "onCreate: No token for service")
            stopSelf()
        } else {
            Log.d(TAG, "onCreate: has token")
            latLngRequest.observeForever {
                d?.dispose()
                api?.let { a ->
                    d = a.apiService.setLocation(makeToken(token!!), it)
                            .subscribeOn(Schedulers.io())
                            .retry()
                            .subscribe(
                                    { loc ->
                                        Log.d(TAG, "onCreate: sent new Location: ${loc.lat} ${loc.lng}")
                                    },
                                    { error ->
                                        Log.e(TAG, "onCreate: error", error)
                                    }
                            )
                }

            }
        }

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        token?.let {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {
//                val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1f) {
//                    latLngRequest.postValue(LatLngRequest(it.latitude, it.longitude))
//                }
//            } else {
//                Log.d(TAG, "onStartCommand: permission denied")
//                stopSelf()
//            }
            getLocation()
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        getLocation()
        return null
    }

    override fun onUnbind(intent: Intent?): Boolean {
        stopUsingGPS()
        return super.onUnbind(intent)
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: service destroyed")
        d?.dispose()
        token?.let {
            api?.let { a ->
                d = a.apiService.deleteLocation(makeToken(it))
                        .subscribeOn(Schedulers.io())
                        .retry()
                        .subscribe(
                                {
                                    Log.d(TAG, "onDestroy: delete location completed")
                                },
                                { error ->
                                    Log.e(TAG, "onDestroy: error", error)
                                }
                        )
                d?.dispose()
            }
        }
    }

    fun getLocation(): Location? {
        try {
            locationManager = mContext?.getSystemService(LOCATION_SERVICE) as LocationManager

            // getting GPS status
            isGPSEnabled = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) == true

            // getting network status
            isNetworkEnabled = locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == true
            if (!isGPSEnabled && !isNetworkEnabled) {
                stopSelf()
            } else {
                canGetLocation = true
                if (isNetworkEnabled) {
                    if (ContextCompat.checkSelfPermission(mContext!!, Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {
                        locationManager?.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this)
                        Log.d(TAG, "Network")
                        if (locationManager != null) {
                            location = locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
//                            if (location != null) {
//                                latitude = location!!.latitude
//                                longitude = location!!.longitude
//                            }
                        }
                    }

                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (ContextCompat.checkSelfPermission(mContext!!, Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {
                        if (location == null) {
                            locationManager?.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this)
                            Log.d(TAG, "GPS Enabled")
                            if (locationManager != null) {
                                location = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
//                                if (location != null) {
//                                    latitude = location!!.latitude
//                                    longitude = location!!.longitude
//                                }
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i("Error", e.message!!)
        }
        return location
    }

    fun stopUsingGPS() {
        locationManager?.removeUpdates(this)
    }

    private fun initToken(): Boolean {
        token = getSharedPreferences("profile", MODE_PRIVATE).getString("token", null)
        return token != null
    }

    companion object {
        private const val TAG = "LOCATION_SERVICE"
        private const val MIN_TIME_BW_UPDATES = 5000L
        private const val MIN_DISTANCE_CHANGE_FOR_UPDATES = 1f
    }

    override fun onLocationChanged(location: Location) {
        Log.d(TAG, "onLocationChanged: $location")
        latLngRequest.postValue(LatLngRequest(location.latitude, location.longitude))
    }
}