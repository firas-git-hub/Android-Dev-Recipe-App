package Model.RecipeDbService

import Model.entities.Recipe
import Model.entities.RecipesList
import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.androiddevfinalproject.R
import com.example.androiddevfinalproject.recipeList
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RecipeApiService {

    fun getRecipies(cuisineType: String, type: String, context: Context, recyclerView: RecyclerView) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.edamam.com/api/recipes/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(RecipeApiInterface::class.java)
        val call: Call<JsonElement> = service.getRecipies(context.getString(R.string.edamamDBAppKey), context.getString(R.string.edamamDBAppId), type, cuisineType)
        call.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    bindValues(response)
                    recyclerView.adapter!!.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d("Error", t.message.toString())
            }
        })
    }

    private fun bindValues(response: Response<JsonElement>){
        var gson: Gson = Gson()
        var data: JsonObject = gson.fromJson(response.body(), JsonObject::class.java)
        var hits = data.get("hits").asJsonArray
        loadDataintoTable(hits)
    }

    private fun loadDataintoTable(data: JsonArray){
        var tmpRecipe = Recipe("", "", "", "", "", "", "","", 0.0, 0.0)
        data.forEach {
            var recipeObject = it.asJsonObject.get("recipe").asJsonObject
            tmpRecipe.countryType = recipeObject.get("cuisineType").asJsonArray.toString()
            tmpRecipe.calories = recipeObject.get("calories").asDouble
            tmpRecipe.dishType = recipeObject.get("dishType").asJsonArray.toString()
            tmpRecipe.imgUrl = recipeObject.get("image").asString
            tmpRecipe.mealType = recipeObject.get("mealType").asJsonArray.toString()
            tmpRecipe.name = recipeObject.get("label").asString
            tmpRecipe.source = recipeObject.get("source").asString
            tmpRecipe.totalWeight = recipeObject.get("totalWeight").asDouble
            tmpRecipe.ingredients = recipeObject.get("ingredientLines").asJsonArray.toString()
            recipeList.recipes.add(tmpRecipe)
            TODO("manage the smaller arrays and turn them into strings.")
        }
    }
}