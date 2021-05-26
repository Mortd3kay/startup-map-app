package com.skyletto.startappfrontend.common.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.IBinder
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.skyletto.startappfrontend.data.network.ApiRepository
import com.skyletto.startappfrontend.data.network.ApiRepository.makeToken
import com.skyletto.startappfrontend.data.requests.LatLngRequest
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class LocationService(var api: ApiRepository?) : Service() {
    private var token: String? = null
    private val latLngRequest = MutableLiveData<LatLngRequest>()
    private var d : Disposable? = null
    var permissionDeniedListener : PermissionDeniedListener? = null

    constructor():this(null)

    override fun onCreate() {
        super.onCreate()
        if (!initToken()) {
            Log.d(TAG, "onCreate: No token for service")
            stopSelf()
        } else {
            latLngRequest.observeForever {
                d?.dispose()
                api?.let {a->
                    d = a.apiService.setLocation(makeToken(token!!), it)
                            .subscribeOn(Schedulers.io())
                            .retry()
                            .subscribe(
                                    { loc ->
                                        Log.d(TAG, "onCreate: sent new Location: ${loc.lat} ${loc.lng}")
                                    },
                                    { error->
                                        Log.e(TAG, "onCreate: error", error)
                                    }
                            )
                }

            }
        }

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        token?.let {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1f){
                    latLngRequest.postValue(LatLngRequest(it.latitude, it.longitude))
                }
            } else {
                permissionDeniedListener?.onDenied()
                stopSelf()
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        d?.dispose()
        token?.let {
            api?.let { a ->
                d = a.apiService.deleteLocation(makeToken(it))
                        .subscribeOn(Schedulers.io())
                        .retry()
                        .subscribe(
                                {
                                    Log.d(TAG, "onDestroy: delete location completed")
                                    d?.dispose()
                                },
                                { error ->
                                    Log.e(TAG, "onDestroy: error", error)
                                }
                        )
            }
        }
    }

    private fun initToken(): Boolean {
        token = getSharedPreferences("profile", MODE_PRIVATE).getString("token", null)
        return token != null
    }

    companion object {
        private const val TAG = "LOCATION_SERVICE"
    }
}