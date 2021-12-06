package com.example.secondhandapplication.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.secondhandapplication.R
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.MapView


class MapViewActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_view)

        Mapbox.getInstance(this,getString(R.string.mapbox_access_token))
        mapView = findViewById(R.id.map)
        mapView.onCreate(savedInstanceState)
    }
    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if(outState!=null){
            mapView.onSaveInstanceState(outState)
        }
    }

}