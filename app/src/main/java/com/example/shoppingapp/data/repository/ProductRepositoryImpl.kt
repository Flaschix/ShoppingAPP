package com.example.shoppingapp.data.repository

import androidx.lifecycle.viewModelScope
import com.example.onlinemarket.domain.repository.ProductRepository
import com.example.shoppingapp.data.mapper.ProductMapper
import com.example.shoppingapp.data.network.ApiService
import com.example.shoppingapp.domain.entity.Product.Product
import com.example.shoppingapp.presentation.shop_item.AddProductState
import com.example.shoppingapp.util.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val mapper: ProductMapper,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val apiService: ApiService
): ProductRepository {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

//    private val _products = mutableListOf<Product>()
//
//    private val products: List<Product>
//        get() = _products.toList()

    private val recommendations: StateFlow<List<Product>> = flow<List<Product>> {
        val response = apiService.loadListProduct()
        val products = mapper.mapResponseToProduct(response)
        emit(products.toList())
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = listOf()
    )

    override fun getProduct(id: String): StateFlow<Product?> {
        TODO("Not yet implemented")
    }

    override fun getListProduct(): StateFlow<List<Product>> = recommendations

    override fun getListProductByCategory(category: String): StateFlow<List<Product>> = flow<List<Product>> {
        val response = apiService.loadListProduct()
        val products = mapper.mapResponseToProductByCategory(response, category)
        emit(products.toList())
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = listOf()
    )

    private val basketCollection = firestore.collection(Constants.DB_USER).document(auth.uid!!).collection(Constants.DB_BASKET)

    override fun addProductToBasket(product: Product): StateFlow<Boolean> = flow<Boolean> {
        basketCollection
            .whereEqualTo(Constants.DB_PRODUCT_ID, product.id).get()
            .addOnSuccessListener {
                it.documents.let {
                    if (it.isEmpty()){
                        basketCollection.document().set(product)
                            .addOnSuccessListener {
                                coroutineScope.launch {
                                    emit(true)
                                }
                            }
                            .addOnFailureListener{
                                coroutineScope.launch {
                                    emit(false)
                                }
                            }
                    }
                }
            }
            .addOnFailureListener {
                coroutineScope.launch {
                    emit(false)
                }
            }
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = false
    )
}