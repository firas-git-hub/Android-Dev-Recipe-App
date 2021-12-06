package Model.RecipeDbService

import Model.entities.Recipe
import Model.entities.RecipesList
import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.androiddevfinalproject.R
import com.example.androiddevfinalproject.recipeList
import org.json.JSONObject
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
        val call: Call<JSONObject> = service.getRecipies(context.getString(R.string.edamamDBAppKey), context.getString(R.string.edamamDBAppId), type, cuisineType)

        call.enqueue(object : Callback<JSONObject> {
            override fun onResponse(call: Call<JSONObject>, response: Response<JSONObject>) {
                if (response.code() == 200) {
                    bindValues(response)
                    recyclerView.adapter!!.notifyDataSetChanged()
                }
            }
            override fun onFailure(call: Call<RecipesList>, t: Throwable) {
                Log.d("error", t.message.toString())
            }
        })
    }

    private fun bindValues(response: Response<JSONObject>){
        val hits = response.body()!!["hits"] as JSONObject
    }
}