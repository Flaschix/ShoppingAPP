package com.example.shoppingapp.presentation.home.category_type.main

import androidx.lifecycle.ViewModel
import com.example.onlinemarket.domain.usecase.GetProductListUseCase
import com.example.shoppingapp.presentation.home.category_type.RVState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    private val getProductListUseCase: GetProductListUseCase
): ViewModel() {

    private val productList = getProductListUseCase()

    val screenState = productList
        .filter { it.isNotEmpty() }
        .map { RVState.Success(it) as RVState }
        .onStart { emit(RVState.Loading) }
}