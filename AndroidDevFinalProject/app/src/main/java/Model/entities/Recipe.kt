package Model.entities

import com.google.gson.annotations.SerializedName
import java.net.URL

data class Recipe(
    val id: String = "",
    @SerializedName("label") val name: String,
    @SerializedName("cuisineType") val countryType: String,
    @SerializedName("ingredientLines") val ingredients: String,
    @SerializedName("image") val imgUrl: String,
    @SerializedName("source") val source: String,
    @SerializedName("mealType") val mealType: String,
    @SerializedName("dishType") val dishType: String,
    @SerializedName("calories") val calories: Double,
    @SerializedName("totalWeight") val totalWeight: Double
)
