package com.example.shoppingapp.presentation.shop_item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.domain.entity.Product.Product
import com.example.shoppingapp.domain.usecase.AddProductToBasketUseCase
import com.example.shoppingapp.util.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopItemViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val addProductToBasketUseCase: AddProductToBasketUseCase
): ViewModel() {

    private val _addToBasketState = MutableStateFlow<AddProductState>(AddProductState.Initial)
    val addToBasketState = _addToBasketState.asStateFlow()

    private val basketCollection = firestore.collection(Constants.DB_USER).document(auth.uid!!).collection(Constants.DB_BASKET)

    fun addProductToBasket(product: Product){
        viewModelScope.launch { _addToBasketState.emit(AddProductState.Loading) }

        basketCollection
            .whereEqualTo(Constants.DB_PRODUCT_ID, product.id).get()
            .addOnSuccessListener {
                it.documents.let {
                    if (it.isEmpty()){
                        basketCollection.document().set(product)
                            .addOnSuccessListener {
                                viewModelScope.launch {
                                    _addToBasketState.emit(AddProductState.Success)
                                }
                            }
                            .addOnFailureListener{
                                viewModelScope.launch {
                                    _addToBasketState.emit(AddProductState.Error(it.message.toString()))
                                }
                            }
                    }
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _addToBasketState.emit(AddProductState.Error(it.message.toString()))
                }
            }
    }
}