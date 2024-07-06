package com.example.shoppingapp.util

import android.view.View
import androidx.fragment.app.Fragment
import com.example.shoppingapp.R
import com.example.shoppingapp.presentation.activity.AppActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

fun Fragment.hideBottomNavigationView(){
    val bottomNavView = (activity as AppActivity).findViewById<BottomNavigationView>(
        R.id.bottomNavigation
    )

    bottomNavView.visibility = View.GONE
}

fun Fragment.showBottomNavigationView(){
    val bottomNavView = (activity as AppActivity).findViewById<BottomNavigationView>(
        R.id.bottomNavigation
    )

    bottomNavView.visibility = View.VISIBLE
}