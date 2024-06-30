package com.example.shoppingapp.presentation.home.category_type.base

import androidx.lifecycle.ViewModel
import com.example.shoppingapp.domain.usecase.GetListProductByCategoryUseCase
import com.example.shoppingapp.presentation.home.category_type.Category
import com.example.shoppingapp.presentation.home.category_type.RVState
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class BaseCategoryViewModel(
    private val getListProductByCategoryUseCase: GetListProductByCategoryUseCase,
    private val category: Category
): ViewModel(){
    private val productList = getListProductByCategoryUseCase(category.category)



    val screenState = productList
        .filter { it.isNotEmpty() }
        .map { RVState.Success(it) as RVState }
        .onStart { emit(RVState.Loading) }
}