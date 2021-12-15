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
import kotlin.coroutines.suspendCoroutine

object RecipeApiService {

    fun getRecipies(
        cuisineType: String,
        type: String,
        context: Context,
        recyclerView: RecyclerView
    ) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.edamam.com/api/recipes/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(RecipeApiInterface::class.java)
        val call: Call<JsonElement> = service.getRecipies(
            context.getString(R.string.edamamDBAppKey),
            context.getString(R.string.edamamDBAppId),
            type,
            cuisineType
        )
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

    private fun bindValues(response: Response<JsonElement>) {
        var gson = Gson()
        var data: JsonObject = gson.fromJson(response.body(), JsonObject::class.java)
        var hits = data.get("hits").asJsonArray
        loadDataIntoArray(hits)
    }

    private fun loadDataIntoArray(data: JsonArray) {
        var tmpRecipeList: ArrayList<Recipe> = ArrayList()
        data.forEach {
            var tmpRecipe = Recipe("", "", "", "", "", "", "", "", 0.0, 0.0)
            var recipeObject = it.asJsonObject.get("recipe").asJsonObject
            var tmpId = recipeObject.get("uri").asString
            tmpRecipe.id = tmpId.substring(tmpId.lastIndexOf("#recipe_") + 8)
            tmpRecipe.countryType =
                jsonArrayToCustomString(recipeObject.getAsJsonArray("cuisineType"))
            tmpRecipe.calories = recipeObject.get("calories").asDouble
            tmpRecipe.dishType = jsonArrayToCustomString(recipeObject.getAsJsonArray("dishType"))
            tmpRecipe.imgUrl = recipeObject.get("image").asString
            tmpRecipe.mealType = jsonArrayToCustomString(recipeObject.getAsJsonArray("mealType"))
            tmpRecipe.name = recipeObject.get("label").asString
            tmpRecipe.source = recipeObject.get("url").asString
            tmpRecipe.totalWeight = recipeObject.get("totalWeight").asDouble
            tmpRecipe.ingredients =
                jsonArrayToCustomString(recipeObject.getAsJsonArray("ingredientLines"))
            tmpRecipeList.add(tmpRecipe)
        }
        recipeList.recipes = tmpRecipeList
    }

    private fun jsonArrayToCustomString(jsonArray: JsonArray): String {
        var res = ""
        jsonArray.forEach { element -> res += "$element$\r\n" }
        return res.substring(0, res.lastIndexOf("$")).replace("\"", "").replace("[", "")
    }
}