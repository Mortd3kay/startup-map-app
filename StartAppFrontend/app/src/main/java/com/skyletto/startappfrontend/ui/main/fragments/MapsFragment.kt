package com.skyletto.startappfrontend.ui.main.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.skyletto.startappfrontend.R
import com.skyletto.startappfrontend.ui.main.ActivityFragmentChanger

class MapsFragment : Fragment() {
    private lateinit var mMap: GoogleMap
    private var mActivity: ActivityFragmentChanger? = null
    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        try {
            if (!mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.mapstyle))) Log.e(TAG, "onMapReady: Parsing failed")
        } catch (e: Exception) {
            Log.e(TAG, "onMapReady: ", e.cause)
        }
        val sydney = LatLng(-35.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_maps, container, false)
        val fab: FloatingActionButton = v.findViewById(R.id.map_profile_btn)
        fab.setOnClickListener { mActivity?.goToProfile() }
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    fun setActivity(mActivity: ActivityFragmentChanger?) {
        this.mActivity = mActivity
    }

    companion object {
        private const val TAG = "MAP_FRAGMENT"
    }
}