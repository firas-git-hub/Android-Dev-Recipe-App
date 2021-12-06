package Model.entities

import com.google.gson.annotations.SerializedName

data class RecipesList(@SerializedName("data") val recipeData: ArrayList<Recipe>)
