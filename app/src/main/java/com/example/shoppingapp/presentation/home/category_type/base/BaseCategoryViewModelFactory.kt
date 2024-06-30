package com.example.shoppingapp.presentation.home.category_type.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shoppingapp.domain.usecase.GetListProductByCategoryUseCase
import com.example.shoppingapp.presentation.home.category_type.Category

class BaseCategoryViewModelFactory(
    private val getListProductByCategoryUseCase: GetListProductByCategoryUseCase,
    private val category: Category
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BaseCategoryViewModel(getListProductByCategoryUseCase, category) as T
    }
}