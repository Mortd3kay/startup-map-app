package com.skyletto.startappfrontend.ui.main.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.skyletto.startappfrontend.R
import com.skyletto.startappfrontend.common.adapters.RecommendAdapter
import com.skyletto.startappfrontend.common.models.AlertModel
import com.skyletto.startappfrontend.common.models.RecommendationItem
import com.skyletto.startappfrontend.common.utils.LaconicTextWatcher
import com.skyletto.startappfrontend.common.utils.MapViewModelFactory
import com.skyletto.startappfrontend.common.utils.toast
import com.skyletto.startappfrontend.data.requests.LatLngRequest
import com.skyletto.startappfrontend.databinding.FragmentMapsBinding
import com.skyletto.startappfrontend.ui.main.ActivityFragmentWorker
import com.skyletto.startappfrontend.ui.main.MainActivity
import com.skyletto.startappfrontend.ui.main.viewmodels.MapViewModel
import com.skyletto.startappfrontend.ui.main.viewmodels.OnConditionUpdateListener
import com.skyletto.startappfrontend.ui.main.viewmodels.RecommendationCallback
import java.util.function.Predicate


class MapsFragment : Fragment() {
    private lateinit var mMap: GoogleMap
    private lateinit var categories: Spinner
    var mActivity: ActivityFragmentWorker? = null
    private var viewModel: MapViewModel? = null
    private var callback: OnMapReadyCallback = initMapCallback()
    private var sp: SharedPreferences? = null
    private val userMarkers = HashSet<Marker>()
    private val projectMarkers = HashSet<Marker>()
    private val markerModels = HashMap<Marker, AlertModel>()
    private var lastKnownLocation = MutableLiveData<Location>()

    private lateinit var client: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sp = activity?.getSharedPreferences("profile", Activity.MODE_PRIVATE)
        viewModel = activity?.let { ViewModelProvider(it, MapViewModelFactory(it.application, getIdFromSP())).get(MapViewModel::class.java) }
        observeViewModel()
        client = LocationServices.getFusedLocationProviderClient(context)
        lastKnownLocation.observeForever {
            if (it!=null){
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude),13f))
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    Log.d(TAG, "onCreate: postDelayed works")
                    viewModel?.loadRecommendations(LatLngRequest(it.latitude, it.longitude))
                }, 2000)

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

    private fun initLocationListener(){
        if (activity?.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            val locResult = client.lastLocation
            locResult.addOnCompleteListener(activity!!){
                if (it.isSuccessful){
                    lastKnownLocation.postValue(it.result)
                }
            }
        }

    }

    private fun observeViewModel(){
        viewModel?.activity = mActivity
        initViewModelListeners()
        observeUserLocations()
        observeProjectLocations()
    }

    private fun initViewModelListeners(){
        viewModel?.onConditionUpdateListener = object : OnConditionUpdateListener {
            override fun update(predicates: Array<Predicate<AlertModel>?>) {
                for (m in markerModels){
                    predicates[0]?.let {
                        m.key.isVisible = it.test(m.value)
                    }
                    if (m.key.isVisible)
                        predicates[1]?.let {
                            m.key.isVisible = it.test(m.value)
                        }
                }
            }
        }

        viewModel?.recommendationCallback = object : RecommendationCallback {
            override fun callback(list: List<RecommendationItem>) {
                showRecommendationDialog(list)
            }
        }
    }

    private fun observeProjectLocations(){
        viewModel?.projectLocations?.observe(this) {
            for (m in projectMarkers) {
                m.remove()
            }
            projectMarkers.clear()
            for (i in it) {
                val img = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.map_project_icon), 128, 128, false)
                val marker = mMap.addMarker(MarkerOptions().position(LatLng(i.lat, i.lng)).icon(BitmapDescriptorFactory.fromBitmap(img)))
                marker.isVisible = false
                projectMarkers.add(marker)
                markerModels[marker] = AlertModel(
                        i.userId,
                        i.isProject
                )
            }
            viewModel?.updateMarkers()
        }
    }

    private fun observeUserLocations(){
        viewModel?.userLocations?.observe(this) {
            for (m in userMarkers) {
                m.remove()
            }
            userMarkers.clear()
            for (i in it) {
                val img = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.map_profile_icon), 128, 128, false)
                val marker = mMap.addMarker(MarkerOptions().position(LatLng(i.lat, i.lng)).icon(BitmapDescriptorFactory.fromBitmap(img)))
                marker.isVisible = false
                userMarkers.add(marker)
                markerModels[marker] = AlertModel(
                        i.userId,
                        i.isProject
                )
            }
            viewModel?.updateMarkers()
        }
    }


    private fun initViews(b: FragmentMapsBinding) {
        categories = b.tbSpinner
        categories.adapter = context?.let { ArrayAdapter(it, R.layout.spinner_text, resources.getStringArray(R.array.categories)) }
        categories.dropDownHorizontalOffset = 15
        categories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel?.categoryId?.value = id
                viewModel?.updateMarkers()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                viewModel?.categoryId?.postValue(0L)
            }
        }
        b.tbSettings.setOnClickListener { viewModel?.goToSettings() }
        b.addProjectBtn.setOnClickListener {
            if (viewModel?.creationAvailable == true)
                viewModel?.goToCreateProject()
            else toast(context, getString(R.string.you_may_have_only_one_project))
        }
        b.mapSearchField.addTextChangedListener(object : LaconicTextWatcher {
            override fun afterTextChanged(s: Editable) {
                viewModel?.searchField?.set(s.toString())
                viewModel?.updateMarkers()
            }
        })
    }

    private fun initMapCallback() = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        if (activity?.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            mMap?.let {
                it.isMyLocationEnabled = true
                initLocationListener()
            }

        } else requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION),MainActivity.RQST_CODE)

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

        mMap.setOnMarkerClickListener {
            val model = markerModels[it]
            if (model != null) {
                showDialog(model)
                return@setOnMarkerClickListener true
            }
            return@setOnMarkerClickListener false
        }
    }

    private fun showDialog(model: AlertModel){
        val dlg = MapDialog(model)
        val lp = WindowManager.LayoutParams()
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        dlg.show(parentFragmentManager, "DIALOG")
    }

    private fun showRecommendationDialog(list:List<RecommendationItem>){
        Log.d(TAG, "showRecommendationDialog: start creating")
        context?.let {
            val dlgBuilder = AlertDialog.Builder(it)
            val v = LayoutInflater.from(it).inflate(R.layout.recommend_dialog, null)
            val rv = v.findViewById<RecyclerView>(R.id.rec_item_rv)
            rv.layoutManager = GridLayoutManager(it, 2)
            val adapter = RecommendAdapter(list,it)
            adapter.onUsernameClickListener = object : OnUsernameClickListener {
                override fun onClick(userId: Long) {
                    showDialog(AlertModel(userId, false))
                }
            }
            rv.adapter = adapter
            val closeBtn = v.findViewById<ImageView>(R.id.rec_dlg_close_btn)
            dlgBuilder.setView(v)
            val dlg = dlgBuilder.create()
            dlg.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
            dlg.show()
            dlg.window?.setLayout(
                    resources.displayMetrics.widthPixels * 85 / 100,
                    resources.displayMetrics.heightPixels * 60 / 100
            )
            closeBtn.setOnClickListener { dlg.dismiss() }
        }
    }

    private fun getIdFromSP() = sp?.getLong("id", -1)!!

    companion object {
        private const val TAG = "MAP_FRAGMENT"
    }
}