package com.example.shoppingapp.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.shoppingapp.R
import com.example.shoppingapp.databinding.ActivityAppBinding
import com.example.shoppingapp.presentation.basket.BasketState
import com.example.shoppingapp.presentation.basket.BasketViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AppActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityAppBinding.inflate(layoutInflater)
    }

    val viewModel by viewModels<BasketViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navController = findNavController(R.id.appHostFragment)
        binding.bottomNavigation.setupWithNavController(navController)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.basketState.collectLatest{
                    when(it){
                        is BasketState.Success -> {
                            val count = it.data.size
                            val bottomBar = findViewById<BottomNavigationView>(R.id.bottomNavigation)
                            bottomBar.getOrCreateBadge(R.id.basketFragment).apply {
                                number = count
                                backgroundColor = resources.getColor(R.color.light_pink)
                            }
                        }
                        else -> {}
                    }
                }
            }
        }
    }
}