package com.example.androiddevfinalproject

import Model.CheckPermissionsService
import Model.MarkerCoords
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment(activity: FragmentActivity) : Fragment(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    val coarseLocationRQ = 1
    val fineLocationRQ = 2
    var currentActivity = activity
    lateinit var map: GoogleMap
    lateinit var mapFragment: SupportMapFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v: View = inflater.inflate(R.layout.fragment_map, container, false)
        bindVariables(v)
        setEvents()
        return v
    }

    fun bindVariables(v: View) {
        mapFragment = childFragmentManager.findFragmentById(R.id.countriesFragment) as SupportMapFragment
    }

    fun setEvents() {
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        configureMapUI(map)
        loadMarkers(map)
        map.setOnInfoWindowClickListener(this)
    }

    fun loadMarkers(map: GoogleMap){
        for (marker in MarkerCoords.markers) {
            map.addMarker(
                MarkerOptions()
                    .position(LatLng(marker.lat, marker.lng ))
                    .title(marker.name)
            )
        }
    }

    fun configureMapUI(map: GoogleMap){
        if(CheckPermissionsService.checkMapPermissions(currentActivity as AppCompatActivity)){
            if (checkSelfPermission(
                    currentActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    currentActivity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    currentActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    fineLocationRQ
                )
                ActivityCompat.requestPermissions(
                    currentActivity,
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
        recipeList.recipes.clear()
        val intent = Intent(activity!!, RecipesListActivity::class.java)
        intent.putExtra("countryType", p0.title)
        startActivity(intent)
    }
}