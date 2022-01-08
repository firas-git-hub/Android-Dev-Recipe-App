package com.example.androiddevfinalproject

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class RecipeTabPageAdapter(activity: FragmentActivity, private val tabCount: Int) :
    FragmentStateAdapter(activity) {

    var activity = activity
    var mPageReferenceMap = HashMap<Int, Fragment>()
    override fun getItemCount(): Int {
        return tabCount
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> {
                var fragment = MapFragment(activity)
                mPageReferenceMap[position] = fragment
                return mPageReferenceMap[position]!!
            }
            1 -> {
                var fragment = SavedRecipesFragment()
                mPageReferenceMap[position] = fragment
                return mPageReferenceMap[position]!!
            }
            else -> {
                var fragment = MapFragment(activity)
                mPageReferenceMap[position] = fragment
                return mPageReferenceMap[position]!!
            }
        }
    }

    fun refreshFragment(key: Int) {
        if (key == 1)
            mPageReferenceMap[key] = SavedRecipesFragment()
    }
}