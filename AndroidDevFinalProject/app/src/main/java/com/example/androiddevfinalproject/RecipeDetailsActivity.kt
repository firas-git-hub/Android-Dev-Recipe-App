package com.example.androiddevfinalproject

import Model.RecipeDbService.FirebaseService
import Model.CameraServices
import Model.entities.Recipe
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import kotlin.coroutines.*
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide

class RecipeDetailsActivity : AppCompatActivity() {

    lateinit var webView: WebView
    lateinit var recipeImageView: ImageView
    lateinit var recipeTextView: TextView
    lateinit var recipeSaveButton: Button
    lateinit var recipeWebhookButton: Button
    lateinit var recipeIngredientsListView: ListView
    lateinit var goBackButton: Button
    var CHANNEL_ID = "RecipeDetailsChannel"
    var notificationIdIndex = 0
    var fbService = FirebaseService()
    var recipeIndex: Int = -1
    val cameraPermissionRQ = 103
    val firebaseService = FirebaseService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_details)
        bindVariables()
        loadrecipe(recipeList.recipes[recipeIndex])
    }

    fun bindVariables() {
        webView = findViewById(R.id.recipeDetailsWebView)
        recipeIndex = intent.getIntExtra("recipeIndex", -1)
        recipeImageView = findViewById(R.id.recipeDetailsIV)
        if (intent.getBooleanExtra("fromSavedRecipe", false)) {
            recipeImageView.setOnClickListener {
                if (CameraServices.checkPermission(this)) {
                    CameraServices.loadCamera(this, cameraPermissionRQ)
                } else {
                    CameraServices.requestPermission(this, cameraPermissionRQ)
                }
            }
        }
        recipeTextView = findViewById(R.id.recipeDetailsName)
        recipeSaveButton = findViewById(R.id.recipeDetailsSaveBut)
        recipeWebhookButton = findViewById(R.id.recipeDetailsWebhookBut)
        recipeIngredientsListView = findViewById(R.id.recipeDetailsIngList)
        goBackButton = findViewById(R.id.goBackFromRecipeDetails)
        goBackButton.setOnClickListener {
            finish()
        }
    }

    fun loadrecipe(recipe: Recipe) {
        this.webView.webViewClient = WebViewClient()
        this.webView.loadUrl(recipe.source)
        if (recipeIndex != -1) {
            var url = recipe.imgUrl
            if (url != null) {
                Glide.with(applicationContext)
                    .load(url)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(recipeImageView)
            }
            firebaseService.getImageFromFirebaseStorage(recipe.id, recipeImageView)
            recipeTextView.text = recipe.name
            var recipeIngredients: ArrayList<String> = ArrayList(recipe.ingredients.split("$"))
            var ingredientsAdapter: ArrayAdapter<String> =
                ArrayAdapter(this, android.R.layout.simple_list_item_1, recipeIngredients)
            recipeIngredientsListView.adapter = ingredientsAdapter
        }
        recipeSaveButton.setOnClickListener {
            saveRecipeToFirebase(recipe)
        }
        recipeWebhookButton.setOnClickListener {
            val source = recipe.source
            if (source != null) {
                val openUrl = Intent(Intent.ACTION_VIEW, Uri.parse(source))
                startActivity(openUrl)

                sendNotification(recipe)
            } else
                Toast.makeText(this, "Link is broken :( .", Toast.LENGTH_SHORT).show()
        }
    }


    fun saveRecipeToFirebase(recipe: Recipe) {
        fbService.writeRecipeToDb(recipe, applicationContext)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == cameraPermissionRQ && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            recipeImageView.setImageBitmap(imageBitmap)
            firebaseService.writeImageToFirebaseStorage(
                imageBitmap,
                recipeList.recipes[recipeIndex].id
            )
        }
    }

    private fun createNotification(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.recipeDetailsActivityNotificationChannelName)
            val descriptionText = getString(R.string.recipeDetailsActivityNotificationChannelText)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(recipe: Recipe) {
        createNotification()
        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentTitle(getString(R.string.recipeDetailsNotificationTitle))
            .setContentText(getString(R.string.recipeDetailsNotificationDescription))
            .setLargeIcon(recipeImageView.drawable.toBitmap())
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(recipe.ingredients.replace("$", "")))

        with(NotificationManagerCompat.from(this)) {
            notify(notificationIdIndex, builder.build())
        }
    }
}