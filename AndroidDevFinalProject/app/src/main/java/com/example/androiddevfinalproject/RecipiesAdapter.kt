package com.example.androiddevfinalproject

import Model.Recipie
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecipiesAdapter() : RecyclerView.Adapter<RecipiesAdapter.ViewHolder>() {

    class ViewHolder(resultView: View) : RecyclerView.ViewHolder(resultView){
        val recipieImage = resultView.findViewById<ImageView>(R.id.recipieIV)
        val recipieName = resultView.findViewById<TextView>(R.id.recipieNameTV)
        val recipieCountry = resultView.findViewById<TextView>(R.id.recipieCountryTypeTV)
        val recipieDescription = resultView.findViewById<TextView>(R.id.recipieDescriptionTV)
        val recipieButton = resultView.findViewById<Button>(R.id.chosenRecipieBut)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recipie_item_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}

object RecipieList{
    var recipies: ArrayList<Recipie> = ArrayList()
}