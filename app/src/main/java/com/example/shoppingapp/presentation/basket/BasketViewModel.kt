package com.example.shoppingapp.presentation.basket

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.domain.entity.Product.Product
import com.example.shoppingapp.domain.usecase.DeleteProductFromBasketUseCase
import com.example.shoppingapp.domain.usecase.GetBasketUseCase
import com.example.shoppingapp.util.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BasketViewModel @Inject constructor(
    private val getBasketUseCase: GetBasketUseCase,
    private val deleteProductFromBasketUseCase: DeleteProductFromBasketUseCase
): ViewModel() {

    private val basket = getBasketUseCase()

    val basketState = basket
        .catch {  }
        .map { BasketState.Success(it) as BasketState }
        .onStart { BasketState.Loading }

    val productsPrice = basketState.map {
        when(it){
            is BasketState.Success -> sumProductPrice(it.data)
            else -> null
        }
    }

    private fun sumProductPrice(products: List<Product>): Int {
        return products.sumOf { it.price.priceWithDiscount.toInt() }
    }

    fun deleteProductFromBasket(product: Product) {

        viewModelScope.launch {
            deleteProductFromBasketUseCase(product).collect{

            }
        }

    }
}