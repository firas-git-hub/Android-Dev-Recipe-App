package com.example.androiddevfinalproject

import Model.RecipeDbService.FirebaseService
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
    var isSavedRecipesRequest = false
    var firebaseService = FirebaseService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipes_list)

        bindVariables()
        spinnerProgress.visibility = View.VISIBLE
        loadrecipes()
        spinnerProgress.visibility = View.GONE
    }

    fun bindVariables() {
        goBackButton = findViewById(R.id.goBackFromRecipesListBut)
        goBackButton.setOnClickListener {
            finish()
        }
        if (intent.hasExtra("savedRecipes")) {
            if (intent.getBooleanExtra("savedRecipes", false)){
                isSavedRecipesRequest = true
            }
        }
        recipesRecyclerView = findViewById(R.id.recipesRecyclerView)
        recipesRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recipesRecyclerView.adapter = recipesAdapter(this, isSavedRecipesRequest)
        spinnerProgress = findViewById(R.id.recipeListProgressBar)
    }

    fun loadrecipes() {
        if (isSavedRecipesRequest) {
            loadSavedrecipes(recipesRecyclerView)
        } else {
            loadrecipesFromSelectedCountry()
        }

    }

    fun loadSavedrecipes(recipesRV: RecyclerView) {
        firebaseService.readRecipesFromDb(recipesRV)
    }

    fun loadrecipesFromSelectedCountry() {
        RecipeApiService.getRecipies(
            intent.getStringExtra("countryType")!!,
            "public",
            this.applicationContext,
            recipesRecyclerView
        )
    }
}