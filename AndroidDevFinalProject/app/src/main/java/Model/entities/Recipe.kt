package Model.entities

import com.google.gson.annotations.SerializedName
import java.net.URL

data class Recipe(
    var id: String = "",
    @SerializedName("label") var name: String,
    @SerializedName("cuisineType") var countryType: String,
    @SerializedName("ingredientLines") var ingredients: String,
    @SerializedName("image") var imgUrl: String,
    @SerializedName("source") var source: String,
    @SerializedName("mealType") var mealType: String,
    @SerializedName("dishType") var dishType: String,
    @SerializedName("calories") var calories: Double,
    @SerializedName("totalWeight") var totalWeight: Double
)
