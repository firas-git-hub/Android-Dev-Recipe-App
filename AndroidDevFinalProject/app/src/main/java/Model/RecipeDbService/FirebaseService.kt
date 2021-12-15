package Model.RecipeDbService

import Model.entities.Recipe
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.androiddevfinalproject.recipeList
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FirebaseService {

    var firebaseDataBase: FirebaseDatabase = FirebaseDatabase.getInstance();
    val dbRef = firebaseDataBase.reference

    fun writeRecipeToDb(recipe: Recipe, context: Context) {
        if(!dbRef.child("recipes").child(recipe.id).setValue(recipe).isSuccessful)
            Toast.makeText(context, "Recipe saved. Happy cooking!", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(context, "Something went wrong, recipe was not saved.", Toast.LENGTH_SHORT).show()
    }

    fun readRecipesFromDb(recipeRecyclerView: RecyclerView) {
        recipeList.recipes.clear()
        dbRef.addValueEventListener(object : ValueEventListener {
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
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        })
    }

}