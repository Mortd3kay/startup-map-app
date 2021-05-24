package com.skyletto.startappfrontend.ui.project.fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.skyletto.startappfrontend.R
import com.skyletto.startappfrontend.common.utils.convertLatLngToString
import com.skyletto.startappfrontend.common.utils.toast
import com.skyletto.startappfrontend.ui.project.viewmodels.CreateProjectViewModel
import java.util.*


class GetLocationFragment(private val viewModel: CreateProjectViewModel) : Fragment() {
    private var lat: Double? = null
    private var lng: Double? = null
    private val address = MutableLiveData("")
    private lateinit var streetBar : Toolbar
    private lateinit var streetTitle : TextView
    private var marker: Marker? = null
    private lateinit var img:Bitmap
    private val callback = OnMapReadyCallback { googleMap ->

        googleMap.setOnMapClickListener {
            updateAddressInfo(it)
            marker?.remove()
            marker = googleMap.addMarker(MarkerOptions().position(it).draggable(true).icon(BitmapDescriptorFactory.fromBitmap(img)))
        }
        googleMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDragStart(p0: Marker?) {
            }

            override fun onMarkerDrag(p0: Marker?) {
            }

            override fun onMarkerDragEnd(p0: Marker?) {
                marker?.let {
                    updateAddressInfo(it.position)
                }
            }

        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        img = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.map_project_icon),128,128, false)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_get_location, container, false)
        val backBtn = v.findViewById<ImageView>(R.id.location_project_back_btn)
        val okBtn = v.findViewById<ImageView>(R.id.location_project_ok_btn)
        streetBar = v.findViewById(R.id.location_project_title_bar)
        streetTitle = v.findViewById(R.id.location_project_street_title)
        backBtn.setOnClickListener {
            activity?.onBackPressed()
        }

        okBtn.setOnClickListener {
            if (lat!=null && lng!=null && !address.value.isNullOrEmpty()) {
                viewModel.project.get()?.lat = lat as Double
                viewModel.project.get()?.lng = lng as Double
                viewModel.project.get()?.address = address.value
                activity?.onBackPressed()
            } else toast(context, getString(R.string.you_didnt_choose_place))
        }

        address.observeForever{
            if (it.isNotEmpty()){
                streetBar.visibility = View.VISIBLE
                streetTitle.text = it
            } else streetBar.visibility = View.GONE
        }
        return v
    }


    private fun updateAddressInfo(it:LatLng){
        lat = it.latitude
        lng = it.longitude
        context?.let { it1 ->
            address.postValue(convertLatLngToString(it1, it))
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}