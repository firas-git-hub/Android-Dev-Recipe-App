package com.example.androiddevfinalproject

import Model.entities.Recipe
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import java.net.URL

class RecipeDetailsActivity : AppCompatActivity() {

    lateinit var webView: WebView
    lateinit var recipeImageView: ImageView
    lateinit var recipeTextView: TextView
    lateinit var recipeSaveButton: Button
    lateinit var recipeWebhookButton: Button
    lateinit var recipeIngredientsListView: ListView
    
    var recipeIndex: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_details)

        bindVariables()
        setEvents()
        loadrecipe(recipeList.recipes[recipeIndex])
    }

    fun bindVariables(){
        webView = findViewById(R.id.recipeDetailsWebView)
        recipeIndex = intent.getIntExtra("recipeIndex", -1)
        recipeImageView = findViewById(R.id.recipeDetailsIV)
        recipeTextView = findViewById(R.id.recipeDetailsName)
        recipeSaveButton = findViewById(R.id.recipeDetailsSaveBut)
        recipeWebhookButton = findViewById(R.id.recipeDetailsWebhookBut)
        recipeIngredientsListView = findViewById(R.id.recipeDetailsIngList)
    }

    fun setEvents(){

    }
    
    fun loadrecipe(recipe: Recipe){
        this.webView.webViewClient = WebViewClient()
        this.webView.loadUrl(recipe.source)
        if (recipeIndex != -1){
            var url = recipe.imgUrl
            if(url != null){
                Glide.with(applicationContext)
                    .load(url)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(recipeImageView)
            }
            recipeTextView.text = recipe.name
            var recipeIngredients: ArrayList<String> = ArrayList(recipe.ingredients.split("$"))
            var ingAdapter: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_1, recipeIngredients)
            recipeIngredientsListView.adapter = ingAdapter
        }
        recipeSaveButton.setOnClickListener {
            saveRecipeToFirebase(recipe.id)
        }
        recipeWebhookButton.setOnClickListener {
            val source = recipe.source
            if(source != null){
                val openUrl = Intent(Intent.ACTION_VIEW, Uri.parse(source))
                startActivity(openUrl)
            }
            else
                Toast.makeText(this, "Link is broken :( .", Toast.LENGTH_SHORT).show()
        }
    }

    fun loadBitmapFromImage(imgUrl: URL): Bitmap?{
        val imageURL = imgUrl.toString()
        var image: Bitmap? = null
        try {
            val `in` = java.net.URL(imageURL).openStream()
            image = BitmapFactory.decodeStream(`in`)
        }
        catch (e: Exception) {
            Log.e("Error Message", e.message.toString())
            e.printStackTrace()
        }
        return image
    }

    fun saveRecipeToFirebase(id: String){
        TODO("Connect to firebase and decide on the model, then upload the data")
    }
}