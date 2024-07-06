package com.example.shoppingapp.presentation.basket

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.domain.entity.Product.Product
import com.example.shoppingapp.util.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BasketViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
): ViewModel() {

    private val _basketState = MutableStateFlow<BasketState>(BasketState.Initial)

    val basketState = _basketState.asStateFlow()

    private val basketCollection = firestore.collection(Constants.DB_USER).document(auth.uid!!).collection(
        Constants.DB_BASKET)

    val productsPrice = basketState.map {
        when(it){
            is BasketState.Success -> sumProductPrice(it.data)
            else -> null
        }
    }

    private fun sumProductPrice(products: List<Product>): Int {
        return products.sumOf { it.price.priceWithDiscount.toInt() }
    }

    init {
        getBasket()
    }

    private fun getBasket(){
        viewModelScope.launch {
            _basketState.emit(BasketState.Loading)
        }

        basketCollection.addSnapshotListener { value, error ->
            if (value == null || error != null) {
                viewModelScope.launch { _basketState.emit(BasketState.Error(error?.message.toString())) }
            } else {

                basketProductDocuments = value.documents
                val basket = value.toObjects(Product::class.java)

                viewModelScope.launch { _basketState.emit(BasketState.Success(basket)) }
            }
        }
    }

    private var basketProductDocuments = emptyList<DocumentSnapshot>()

    fun deleteProductFromBasket(product: Product){

        val index = if(basketState.value is BasketState.Success)
                (basketState.value as BasketState.Success).data.indexOf(product)
        else null

        if (index != null && index != -1) {
            val documentId = basketProductDocuments[index].id
            basketCollection.document(documentId).delete()
        }

    }
}