package com.nalldev.asry.presentation.ui.maps_story

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.nalldev.asry.R
import com.nalldev.asry.databinding.ActivityStoryMapsBinding
import com.nalldev.asry.domain.models.StoryModel
import com.nalldev.asry.util.UIState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsStoryActivity : AppCompatActivity(), OnMapReadyCallback{

    private lateinit var mMap: GoogleMap
    private val binding by lazy {
        ActivityStoryMapsBinding.inflate(layoutInflater)
    }

    private val viewModel by viewModels<MapsStoryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


        initObserver()
        initView()
        initListener()
    }

    private fun initObserver() = with(viewModel) {
        stories.observe(this@MapsStoryActivity) { uiState ->
            when(uiState) {
                is UIState.Error -> {
                    binding.actionRetry.isVisible = true
                    binding.progressBar.isVisible = false
                }
                is UIState.Loading -> {
                    binding.actionRetry.isVisible = false
                    binding.progressBar.isVisible = true
                }
                is UIState.Success -> {
                    binding.actionRetry.isVisible = true
                    binding.progressBar.isVisible = false

                    createStoryMarkers(uiState.data)
                }
            }
        }

        toastEvent.observe(this@MapsStoryActivity) { message ->
            Toast.makeText(this@MapsStoryActivity, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun createStoryMarkers(storyData: List<StoryModel>) {
        mMap.clear()
        storyData.forEach { story ->
            if (story.lat == null || story.lon == null) return@forEach

            val latLng = LatLng(story.lat, story.lon)
            mMap.addMarker(MarkerOptions().position(latLng).title(story.name).snippet(story.description))
        }
    }

    private fun initView() {
        try {
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.maps_styles))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-6.200000, 106.816666), 5f))

        viewModel.fetchStoryWithCoordinate()
    }

    private fun initListener() {
        binding.actionRetry.setOnClickListener {
            viewModel.fetchStoryWithCoordinate()
        }

        binding.actionBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        mMap.setOnMarkerClickListener { marker ->
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.position, 15f))
            marker.showInfoWindow()
            true
        }
    }
}