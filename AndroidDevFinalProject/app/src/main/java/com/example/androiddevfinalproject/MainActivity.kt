package com.example.androiddevfinalproject

import Model.CheckPermissionsService
import Model.MarkerCoords
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    private val coarseLocationRQ = 1
    private val fineLocationRQ = 2
    lateinit var recipeViewPager: ViewPager2
    lateinit var recipeTabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindVariables()
        if(chkPermissions()) {
            setEvents()
            setupTabBar()
        }
        else {
            finishAffinity()
        }
    }

    fun bindVariables(){

        recipeViewPager = findViewById(R.id.recipeViewPager)
        recipeTabLayout = findViewById(R.id.recipeTabLayout)
    }

    fun setEvents(){
        recipeViewPager.isUserInputEnabled = false
    }

    fun chkPermissions(): Boolean{
        if(CheckPermissionsService.checkMapPermissions(this)){
            return true
        }
        else {
            CheckPermissionsService.requestMapPermissions(this, fineLocationRQ, coarseLocationRQ)
            Toast.makeText(applicationContext, "Please Reload the application.", Toast.LENGTH_SHORT).show()
            return false
        }
    }

    fun setupTabBar() {
        val adapter = RecipeTabPageAdapter(this, recipeTabLayout.tabCount)
        recipeViewPager.adapter = adapter
        recipeViewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                recipeTabLayout.selectTab(recipeTabLayout.getTabAt(position))
            }
        })

        recipeTabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab) {
                if(tab.position == 1){
                    adapter.refreshFragment(tab.position)
                    recipeViewPager.adapter!!.notifyItemChanged((tab.position))
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                recipeViewPager.currentItem = tab.position
            }
        })
    }
}