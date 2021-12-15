package com.example.androiddevfinalproject

import Model.entities.Recipe
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class recipesAdapter(mContext: Context) : RecyclerView.Adapter<recipesAdapter.ViewHolder>() {

    val context = mContext

    class ViewHolder(resultView: View) : RecyclerView.ViewHolder(resultView){
        val recipeImgIV = resultView.findViewById<ImageView>(R.id.recipeIV)
        val recipeNameTV = resultView.findViewById<TextView>(R.id.recipeNameTV)
        val recipeCountryTV = resultView.findViewById<TextView>(R.id.recipeCountryTypeTV)
        val recipeDescriptionTV = resultView.findViewById<TextView>(R.id.recipeDescriptionTV)
        val recipeButton = resultView.findViewById<Button>(R.id.chosenrecipeBut)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recipe_item_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var url = recipeList.recipes[position].imgUrl
        if(url != null){
            Glide.with(context)
                .load(url)
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.recipeImgIV)
        }
        holder.recipeNameTV.text = recipeList.recipes[position].name
        holder.recipeCountryTV.text = recipeList.recipes[position].countryType.replace("$", ",")
        holder.recipeDescriptionTV.text = recipeList.recipes[position].mealType + ", " + recipeList.recipes[position].calories + " cals"
        holder.recipeButton.setOnClickListener {
            val intent = Intent(context, RecipeDetailsActivity::class.java)
            intent.putExtra("recipeIndex", position)
            startActivity(context,intent,null)
        }
    }

    override fun getItemCount(): Int {
        return  recipeList.recipes.size
    }
}

object recipeList{
    var recipes: ArrayList<Recipe> = ArrayList()
}