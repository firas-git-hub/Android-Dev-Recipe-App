package com.example.androiddevfinalproject

import Model.RecipeDbService.FirebaseService
import Model.entities.Recipe
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class recipesAdapter(mContext: Context, isSavedRecipeLayout: Boolean, activity: AppCompatActivity) :
    RecyclerView.Adapter<recipesAdapter.ViewHolder>() {
    val isSavedRecipe = isSavedRecipeLayout
    val context = mContext
    val activity = activity
    val firebaseService = FirebaseService()


    class ViewHolder(resultView: View) : RecyclerView.ViewHolder(resultView) {
        val view = resultView
        lateinit var deleteRecipeButton: Button
        lateinit var savedDetialsRecipeButton: Button
        val recipeImgIV = resultView.findViewById<ImageView>(R.id.recipeIV)
        val recipeNameTV = resultView.findViewById<TextView>(R.id.recipeNameTV)
        val recipeCountryTV = resultView.findViewById<TextView>(R.id.recipeCountryTypeTV)
        val recipeDescriptionTV = resultView.findViewById<TextView>(R.id.recipeDescriptionTV)
        val recipeButton = resultView.findViewById<Button>(R.id.chosenRecipeButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (isSavedRecipe) {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.saved_recipe_item_layout, parent, false)
            return ViewHolder(v)
        } else {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.recipe_item_layout, parent, false)
            return ViewHolder(v)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var url = recipeList.recipes[position].imgUrl
        if (url != null) {
            Glide.with(context)
                .load(url)
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.recipeImgIV)
        }
        if (isSavedRecipe) {
            firebaseService.getImageFromFirebaseStorage(
                recipeList.recipes[position].id,
                holder.recipeImgIV
            )
            holder.deleteRecipeButton = holder.view.findViewById(R.id.deleteRecipeButton)
            holder.deleteRecipeButton.setOnClickListener {
                firebaseService.deleteRecipeFromDB(position, context)
                recipeList.recipes.removeAt(position)
                this.notifyDataSetChanged()
            }
            holder.savedDetialsRecipeButton = holder.view.findViewById(R.id.savedChosenRecipeButton)
            holder.savedDetialsRecipeButton.setOnClickListener {
                val intent = Intent(context, RecipeDetailsActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("recipeIndex", position)
                intent.putExtra("fromSavedRecipe", true)
                startActivity(context, intent, null)
            }
        } else {
            holder.recipeButton.setOnClickListener {
                val intent = Intent(context, RecipeDetailsActivity::class.java)
                intent.putExtra("recipeIndex", position)
                startActivity(context, intent, null)
            }
        }
        holder.recipeNameTV.text = recipeList.recipes[position].name
        holder.recipeCountryTV.text = recipeList.recipes[position].countryType.replace("$", ",")
        holder.recipeDescriptionTV.text =
            recipeList.recipes[position].mealType + ", " + recipeList.recipes[position].calories + " cals"
    }

    override fun getItemCount(): Int {
        return recipeList.recipes.size
    }
}

object recipeList {
    var recipes: ArrayList<Recipe> = ArrayList()
}