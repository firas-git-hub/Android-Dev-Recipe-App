package com.example.androiddevfinalproject

import Model.RecipeDbService.FirebaseService
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SavedRecipesFragment : Fragment() {

    lateinit var goBackButton: Button
    lateinit var recipesRecyclerView: RecyclerView
    lateinit var spinnerProgress: ProgressBar
    var firebaseService = FirebaseService()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var v = inflater.inflate(R.layout.fragment_saved_recipes, container, false)
        bindVariables(v)
        loadSavedRecipes(v)
        return v
    }

    fun bindVariables(v: View){
        goBackButton = v.findViewById(R.id.goBackFromSavedRecipesListBut)
        goBackButton.setOnClickListener {
            activity!!.finish()
        }
        recipesRecyclerView = v.findViewById(R.id.savedRecipesRecyclerView)
        recipesRecyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        recipesRecyclerView.adapter = recipesAdapter(activity!!.applicationContext, true, activity as AppCompatActivity)
        spinnerProgress = v.findViewById(R.id.savedRecipeListProgressBar)
    }

    fun loadSavedRecipes(v: View) {
        recipeList.recipes.clear()
        spinnerProgress.isVisible = true
        firebaseService.readRecipesFromDb(recipesRecyclerView, spinnerProgress)
    }
}