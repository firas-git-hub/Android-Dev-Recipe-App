package Model.RecipeDbService

import Model.entities.Recipe
import android.R.attr
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.androiddevfinalproject.recipeList
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import android.R.attr.bitmap
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.widget.ImageView
import androidx.core.app.NotificationCompat
import com.example.androiddevfinalproject.MainActivity
import com.example.androiddevfinalproject.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.io.ByteArrayOutputStream
import java.lang.Exception


class FirebaseService : FirebaseMessagingService() {

    var storageRef = FirebaseStorage.getInstance().reference
    var firebaseDataBase: FirebaseDatabase = FirebaseDatabase.getInstance()
    val dbRef = firebaseDataBase.reference
    val channelId = "FirebaseServiceFCM"
    private val activityRQ: Int = 0

    override fun onMessageReceived(remoteMsg: RemoteMessage) {
        super.onMessageReceived(remoteMsg)
        if(remoteMsg.notification != null){
            generateNotification(remoteMsg.notification!!.title!!, remoteMsg.notification!!.body!!)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("New Token", "New token: $token")
    }

    fun generateNotification(title: String, message: String){
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent= PendingIntent.getActivity(this, activityRQ, intent, PendingIntent.FLAG_ONE_SHOT)
        val builder = NotificationCompat.Builder(
            applicationContext,
            getString(R.string.FirebaseFCMChannelId))
            .setSmallIcon(R.drawable.application_logo)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(getString(R.string.FirebaseFCMChannelId), getString(R.string.FirebaseFCMChannelId), NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(0, builder.build())
    }

    fun writeRecipeToDb(recipe: Recipe, context: Context) {
        var tmpList = recipeList.recipes.toMutableList()
        if (!dbRef.child("recipes").child(recipe.id).setValue(recipe).isSuccessful)
            Toast.makeText(context, "Recipe saved. Happy cooking!", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(
                context,
                "Something went wrong, recipe was not saved.",
                Toast.LENGTH_SHORT
            ).show()
    }

    fun readRecipesFromDb(recipeRecyclerView: RecyclerView, spinner: ProgressBar) {
        recipeList.recipes.clear()
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var data = dataSnapshot.child("recipes").children.forEach {
                    var tmpRecipe = Recipe(
                        it.child("id").value.toString(),
                        it.child("name").value.toString(),
                        it.child("countryType").value.toString(),
                        it.child("ingredients").value.toString(),
                        it.child("imgUrl").value.toString(),
                        it.child("source").value.toString(),
                        it.child("mealType").value.toString(),
                        it.child("dishType").value.toString(),
                        it.child("calories").value as Double,
                        it.child("totalWeight").value as Double
                    )
                    recipeList.recipes.add(tmpRecipe)
                }
                recipeRecyclerView.adapter!!.notifyDataSetChanged()
                spinner.isVisible = false
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        })
    }

    fun deleteRecipeFromDB(position: Int, context: Context) {
        var tmpRecipe = recipeList.recipes[position]
        if (dbRef.child("recipes").child(tmpRecipe.id) != null) {
            dbRef.child("recipes").child(tmpRecipe.id).removeValue()
        } else
            Toast.makeText(context, "Looks like the recipe is already deleted.", Toast.LENGTH_SHORT)
                .show()
    }

     fun getImageFromFirebaseStorage(recipeId: String, imageView: ImageView) {
        val twoMegaBytes: Long = 2048 * 2048
         try{
             storageRef.child("savedRecipesImages/$recipeId").getBytes(twoMegaBytes).addOnSuccessListener {
                 image = BitmapFactory.decodeByteArray(it, 0, it.size)
                 imageView.setImageBitmap(image)
             }
         } catch (ex: Exception) {
             Log.e("get Image Fail", ex.message.toString())
         }

    }

    fun writeImageToFirebaseStorage(image: Bitmap, recipeId: String) {
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data: ByteArray = baos.toByteArray()
        var tmpStorageRef = storageRef.child("savedRecipesImages/$recipeId")
        tmpStorageRef.putBytes(data)
    }

    companion object {
        lateinit var image: Bitmap
    }
}