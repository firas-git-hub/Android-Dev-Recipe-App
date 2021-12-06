package com.example.androiddevfinalproject

import Model.RecipeDbService.RecipeApiService
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecipesListActivity : AppCompatActivity() {

    lateinit var goBackButton: Button
    lateinit var recipesRecyclerView: RecyclerView
    var isSavedrecipesRequest = false
    
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipes_list)

        bindVariables()
        setEvents()
        loadrecipes()
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

    }
}