package Model.RecipeDbService

import Model.entities.Recipe
import Model.entities.RecipesList
import com.google.gson.JsonElement
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.*

interface RecipeApiInterface {
    //https://api.edamam.com/api/recipes/v2
    @GET("https://api.edamam.com/api/recipes/v2")
    fun getRecipes(
        @Query("app_key") applicationKey: String,
        @Query("app_id") applicationId: String,
        @Query("type") type: String,
        @Query("cuisineType") cuisineType: String
    ): Call<JsonElement>
}