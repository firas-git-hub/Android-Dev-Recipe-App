package com.example.androiddevfinalproject

import Model.CheckPermissionsService
import Model.DummyMarkerCoords
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    val coarseLocationRQ = 1
    val fineLocationRQ = 2
    lateinit var mapFragment: SupportMapFragment
    lateinit var map: GoogleMap
    lateinit var goToSavedRecipesButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindVariables()
        if(chkPermissions()) {
            setEvents()
        }
        else {
            finishAffinity()
        }
    }

    fun bindVariables(){
        mapFragment = SupportMapFragment.newInstance()
        goToSavedRecipesButton = findViewById(R.id.viewRecipesButt)
        goToSavedRecipesButton.setOnClickListener{
            intent = Intent(this, RecipesListActivity::class.java)
            intent.putExtra("savedrecipes", true)
            startActivity(intent)
        }
    }

    fun setEvents(){
        mapFragment = supportFragmentManager
            .findFragmentById(R.id.countriesFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    fun chkPermissions(): Boolean{
        if(CheckPermissionsService.checkMapPermissions(this)){
            return true
        }
        else {
            CheckPermissionsService.requestMapPermissions(this, fineLocationRQ, coarseLocationRQ)
            return false
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        configureMapUI(map)
        loadMarkers(map)
        map.setOnInfoWindowClickListener(this)
    }

    fun loadMarkers(map: GoogleMap){
        for (marker in DummyMarkerCoords.markers) {
            map.addMarker(
                MarkerOptions()
                    .position(LatLng(marker.lat, marker.lng ))
                    .title(marker.name)
            )
        }
    }

    fun configureMapUI(map: GoogleMap){
        if(CheckPermissionsService.checkMapPermissions(this)){
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    fineLocationRQ
                )
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    coarseLocationRQ
                )
            }
            else{
                map.isMyLocationEnabled = true
            }
        }
        with(map.uiSettings){
            isCompassEnabled = true
            isZoomControlsEnabled = true
            isMyLocationButtonEnabled = true
        }
    }

    companion object {
        var MarkerList: ArrayList<Marker> = ArrayList()
    }

    override fun onInfoWindowClick(p0: Marker) {
        val intent = Intent(this, RecipesListActivity::class.java)
        startActivity(intent)
    }
}