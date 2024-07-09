package com.example.shoppingapp.presentation.shop_item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.domain.entity.Product.Product
import com.example.shoppingapp.domain.entity.ResultNet
import com.example.shoppingapp.domain.usecase.AddProductToBasketUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopItemViewModel @Inject constructor(
    private val addProductToBasketUseCase: AddProductToBasketUseCase
): ViewModel() {

    private val _addToBasketState = MutableStateFlow<AddProductState>(AddProductState.Initial)
    val addToBasketState = _addToBasketState.asStateFlow()

    fun addProductToBasket(product: Product){
        viewModelScope.launch {
            addProductToBasketUseCase(product).collect { result ->
                when (result) {
                    ResultNet.Loading -> _addToBasketState.emit(AddProductState.Loading)
                    is ResultNet.Success<*> -> _addToBasketState.emit(AddProductState.Success)
                    is ResultNet.Error -> _addToBasketState.emit(AddProductState.Error(result.message))
                    ResultNet.Initial -> _addToBasketState.emit(AddProductState.Loading)
                }
            }
        }
    }
}