package Model.entities

import java.net.URL

data class Recipe(val id: String, val name: String, val countryType: String, val description: String, val imgUrl: URL)
