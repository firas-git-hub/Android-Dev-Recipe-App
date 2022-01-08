package com.example.androiddevfinalproject

import Model.RecipeDbService.FirebaseService
import Model.RecipeDbService.RecipeApiService
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.Exception

class RecipesListActivity : AppCompatActivity() {

    lateinit var goBackButton: Button
    lateinit var recipesRecyclerView: RecyclerView
    lateinit var spinnerProgress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipes_list)

        bindVariables()
        spinnerProgress.visibility = View.VISIBLE
        loadrecipes()
    }

    fun bindVariables() {
        goBackButton = findViewById(R.id.goBackFromRecipesListBut)
        goBackButton.setOnClickListener {
            finish()
        }
        recipesRecyclerView = findViewById(R.id.recipesRecyclerView)
        recipesRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recipesRecyclerView.adapter = recipesAdapter(this, false, this)
        spinnerProgress = findViewById(R.id.recipeListProgressBar)
    }

    fun loadrecipes() {
        loadrecipesFromSelectedCountry()
    }

    fun loadrecipesFromSelectedCountry() {
        RecipeApiService.getRecipes(
            intent.getStringExtra("countryType")!!,
            "public",
            this.applicationContext,
            recipesRecyclerView,
            spinnerProgress
        )
    }
}