package com.skyletto.startappfrontend.ui.main.fragments

import android.app.Activity
import android.content.SharedPreferences
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
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.skyletto.startappfrontend.R
import com.skyletto.startappfrontend.common.utils.MapViewModelFactory
import com.skyletto.startappfrontend.databinding.FragmentMapsBinding
import com.skyletto.startappfrontend.ui.main.ActivityFragmentWorker
import com.skyletto.startappfrontend.ui.main.viewmodels.MapViewModel

class MapsFragment : Fragment() {
    private lateinit var mMap: GoogleMap
    private lateinit var categories: Spinner
    var mActivity: ActivityFragmentWorker? = null
    private var viewModel: MapViewModel? = null
    private var callback: OnMapReadyCallback = initMapCallback()
    private var sp:SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sp = activity?.getSharedPreferences("profile", Activity.MODE_PRIVATE)
        viewModel = activity?.let { ViewModelProvider(it,MapViewModelFactory(it.application, getIdFromSP())).get(MapViewModel::class.java) }
        viewModel?.activity = mActivity
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
        categories.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel?.categoryId = id
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        b.tbSettings.setOnClickListener { viewModel?.goToSettings()}
        b.addProjectBtn.setOnClickListener { viewModel?.goToCreateProject() }
    }


    private fun initMapCallback() = OnMapReadyCallback { googleMap ->
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

    private fun getIdFromSP() = sp?.getLong("id", -1)!!

    companion object {
        private const val TAG = "MAP_FRAGMENT"
    }
}