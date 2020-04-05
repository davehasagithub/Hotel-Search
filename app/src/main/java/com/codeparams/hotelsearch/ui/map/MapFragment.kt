package com.codeparams.hotelsearch.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.codeparams.hotelsearch.R
import com.codeparams.hotelsearch.databinding.MapFragmentBinding
import com.codeparams.hotelsearch.ui.MainActivityViewModel
import com.codeparams.hotelsearch.util.UiUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class MapFragment : DaggerFragment(), OnMapReadyCallback {

    private var initialized = false

    private lateinit var binding: MapFragmentBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mainActivityViewModel: MainActivityViewModel

    private var map: GoogleMap? = null

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(BUNDLE_KEY_INITIALIZED, initialized)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialized = savedInstanceState?.getBoolean(BUNDLE_KEY_INITIALIZED) ?: false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MapFragmentBinding.inflate(inflater)
        UiUtils.doPostponedTransition(this, binding)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.let { actionBar ->
            actionBar.subtitle = getString(R.string.subtitle_maps)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.let { activity ->
            mainActivityViewModel = ViewModelProvider(activity, viewModelFactory).get(
                MainActivityViewModel::class.java
            )
        }

        (childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment)?.getMapAsync(this@MapFragment)

        addViewModelObservers()
    }

    private fun addViewModelObservers() {
        mainActivityViewModel.hotelsForMapLiveData?.observe(
            viewLifecycleOwner,
            Observer { addMarkers() })
    }

    override fun onMapReady(map: GoogleMap?) {
        this.map = map

        if (!initialized) {
            initialized = true
            map?.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        INITIAL_LAT,
                        INITIAL_LONG
                    ), INITIAL_ZOOM
                )
            )
        }

        addMarkers()
    }

    private fun addMarkers() {
        mainActivityViewModel.hotelsForMapLiveData?.value?.forEach { hotel ->
            map?.addMarker(
                MarkerOptions()
                    .position(LatLng(hotel.latitude, hotel.longitude))
                    .title(hotel.name)
            )
        }
    }

    companion object {
        const val BUNDLE_KEY_INITIALIZED = "initialized"

        const val INITIAL_LAT = 41.884510
        const val INITIAL_LONG = -87.787898
        const val INITIAL_ZOOM = 10.0f

        fun newInstance(): MapFragment =
            MapFragment()
    }
}
