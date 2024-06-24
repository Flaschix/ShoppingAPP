package com.example.shoppingapp.presentation.home.category_type.main

import androidx.lifecycle.ViewModel
import com.example.onlinemarket.domain.usecase.GetProductListUseCase
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
        .map { MainCategoryRVState.Success(it) as MainCategoryRVState }
        .onStart { emit(MainCategoryRVState.Loading) }
}