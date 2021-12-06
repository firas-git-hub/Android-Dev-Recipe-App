package Model.RecipeDbService

import Model.entities.Recipe
import Model.entities.RecipesList
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApiInterface {
    //https://api.edamam.com/api/recipes/v2
    @GET("https://api.edamam.com/api/recipes/v2")
    fun getRecipies(
        @Query("app_key") applicationKey: String,
        @Query("app_id") applicationId: String,
        @Query("type") type: String,
        @Query("cuisineType") cuisineType: String
    ): Call<JSONObject>
}