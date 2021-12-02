package com.example.androiddevfinalproject

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.bumptech.glide.Glide
import java.net.URL

class recipeDetailsActivity : AppCompatActivity() {

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
        loadrecipe()
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
    
    fun loadrecipe(){
        if (recipeIndex != -1){
            recipeImageView.setImageBitmap(loadBitmapFromImage(recipeList.recipes[recipeIndex].imgUrl))
            recipeTextView.text = recipeList.recipes[recipeIndex].name

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
}