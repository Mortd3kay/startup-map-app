package com.skyletto.startappfrontend.ui.main.fragments

import android.app.Activity
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.skyletto.startappfrontend.R
import com.skyletto.startappfrontend.common.utils.MapViewModelFactory
import com.skyletto.startappfrontend.common.utils.toast
import com.skyletto.startappfrontend.databinding.FragmentMapsBinding
import com.skyletto.startappfrontend.ui.main.ActivityFragmentWorker
import com.skyletto.startappfrontend.ui.main.viewmodels.MapViewModel
import io.reactivex.disposables.Disposable

class MapsFragment : Fragment() {
    private lateinit var mMap: GoogleMap
    private lateinit var categories: Spinner
    var mActivity: ActivityFragmentWorker? = null
    private var viewModel: MapViewModel? = null
    private var callback: OnMapReadyCallback = initMapCallback()
    private var sp: SharedPreferences? = null
    private val userMarkers = HashSet<Marker>()
    private val projectMarkers = HashSet<Marker>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sp = activity?.getSharedPreferences("profile", Activity.MODE_PRIVATE)
        viewModel = activity?.let { ViewModelProvider(it, MapViewModelFactory(it.application, getIdFromSP())).get(MapViewModel::class.java) }
        viewModel?.activity = mActivity
        viewModel?.userLocations?.observe(this) {
            Log.d(TAG, "user locations: $it")
            for (m in userMarkers) {
                m.remove()
            }
            userMarkers.clear()
            for (i in it) {
                val img = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.map_bug_icon), 128, 128, false)
                userMarkers.add(mMap.addMarker(MarkerOptions().position(LatLng(i.lat, i.lng)).icon(BitmapDescriptorFactory.fromBitmap(img))))
            }
        }
        viewModel?.projectLocations?.observe(this) {
            Log.d(TAG, "project locations: $it")
            for (m in projectMarkers) {
                m.remove()
            }
            projectMarkers.clear()
            for (i in it) {
                val img = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.map_project_icon), 128, 128, false)
                projectMarkers.add(mMap.addMarker(MarkerOptions().position(LatLng(i.lat, i.lng)).icon(BitmapDescriptorFactory.fromBitmap(img))))
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = DataBindingUtil.inflate<FragmentMapsBinding>(inflater, R.layout.fragment_maps, container, false)
        val v = binding.root
        initViews(binding)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun initViews(b: FragmentMapsBinding) {
        categories = b.tbSpinner
        categories.adapter = context?.let { ArrayAdapter(it, R.layout.spinner_text, resources.getStringArray(R.array.categories)) }
        categories.dropDownHorizontalOffset = 15
        categories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel?.categoryId = id
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        b.tbSettings.setOnClickListener { viewModel?.goToSettings() }
        b.addProjectBtn.setOnClickListener {
            if (viewModel?.creationAvailable == true)
                viewModel?.goToCreateProject()
            else toast(context, getString(R.string.you_may_have_only_one_project))
        }
    }


    private fun initMapCallback() = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        try {
            if (!mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.mapstyle))) Log.e(TAG, "onMapReady: Parsing failed")
        } catch (e: Exception) {
            Log.e(TAG, "onMapReady: ", e.cause)
        }
        mMap.setOnCameraIdleListener {
            Log.d(TAG, "initMapCallback: camera idle")
            viewModel?.loadLocations(mMap.cameraPosition.target, mMap.cameraPosition.zoom)
            viewModel?.loadProjectLocations(mMap.cameraPosition.target, mMap.cameraPosition.zoom)
        }
    }

    private fun getIdFromSP() = sp?.getLong("id", -1)!!

    companion object {
        private const val TAG = "MAP_FRAGMENT"
    }
}