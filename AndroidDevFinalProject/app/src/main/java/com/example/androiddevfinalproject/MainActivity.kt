package com.example.androiddevfinalproject

import Model.CheckPermissionsService
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    val coarseLocationRQ = 1
    val fineLocationRQ = 2
    lateinit var mapFragment: SupportMapFragment
    lateinit var goToRecipesButton: Button

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
        goToRecipesButton = findViewById(R.id.viewRecipesButt)
        goToRecipesButton.setOnClickListener{
            intent = Intent(this, RecipiesListActivity::class.java)
            startActivity(intent)
        }
    }

    fun setEvents(){
        supportFragmentManager
            .beginTransaction()
            .add(R.id.countriesFragment, mapFragment)
            .commit()
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

    override fun onMapReady(map: GoogleMap) {

    }

    companion object {
        var MarkerList: ArrayList<Marker> = ArrayList()
    }
}