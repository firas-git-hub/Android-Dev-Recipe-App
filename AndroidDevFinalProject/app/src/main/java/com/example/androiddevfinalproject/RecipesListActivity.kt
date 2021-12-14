package com.example.androiddevfinalproject

import Model.RecipeDbService.RecipeApiService
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecipesListActivity : AppCompatActivity() {

    lateinit var goBackButton: Button
    lateinit var recipesRecyclerView: RecyclerView
    lateinit var spinnerProgress: ProgressBar
    var isSavedrecipesRequest = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipes_list)

        bindVariables()
        setEvents()
        spinnerProgress.visibility = View.VISIBLE
        loadrecipes()
        spinnerProgress.visibility = View.GONE
    }

    fun bindVariables() {
        goBackButton = findViewById(R.id.goBackFromRecipesListBut)
        goBackButton.setOnClickListener {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        recipesRecyclerView = findViewById(R.id.recipesRecyclerView)
        recipesRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recipesRecyclerView.adapter = recipesAdapter(this)
        spinnerProgress = findViewById(R.id.recipeListProgressBar)
    }

    fun setEvents() {

    }

    fun loadrecipes(){
        RecipeApiService.getRecipies(intent.getStringExtra("countryType")!!, "public", this.applicationContext, recipesRecyclerView)
    }

    fun loadSavedrecipes(){
        if(intent.hasExtra("savedrecipes")){
            isSavedrecipesRequest = intent.getBooleanExtra("savedrecipes", false)
            loadSavedrecipes()
        }
        else{
            loadrecipesFromSelectedCountry()
        }
    }

    fun loadrecipesFromSelectedCountry(){
        TODO("Get the recipes from firebase and load them")
    }
}