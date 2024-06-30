package com.example.shoppingapp.presentation.home.category_type.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.shoppingapp.domain.usecase.GetListProductByCategoryUseCase
import com.example.shoppingapp.presentation.home.category_type.Category
import com.example.shoppingapp.presentation.home.category_type.RVState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ChairFragment: BaseCategoryFragment() {
    @Inject
    lateinit var getListProductByCategoryUseCase: GetListProductByCategoryUseCase

    private val viewModel by viewModels<BaseCategoryViewModel>{
        BaseCategoryViewModelFactory(getListProductByCategoryUseCase, Category.Chair)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.screenState.collect{
                    when(it){
                        is RVState.Error -> {}
                        RVState.Initial -> {}
                        RVState.Loading -> {
                            showProgress()
                        }
                        is RVState.Success -> {
                            offerAdapter.submitList(it.data)
                            bestProductAdapter.submitList(it.data)
                            hideProgress()
                        }
                    }
                }
            }
        }
    }
}