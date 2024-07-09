package com.example.shoppingapp.data.repository

import androidx.lifecycle.viewModelScope
import com.example.onlinemarket.domain.repository.UserRepository
import com.example.shoppingapp.domain.entity.Product.Product
import com.example.shoppingapp.domain.entity.ResultNet
import com.example.shoppingapp.domain.entity.User
import com.example.shoppingapp.presentation.sign_in.SignInState
import com.example.shoppingapp.presentation.sign_in.dialog.ResetPasswordState
import com.example.shoppingapp.presentation.sign_up.SignUpState
import com.example.shoppingapp.util.Constants
import com.example.shoppingapp.util.mergeWith
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
): UserRepository {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val refreshedListFlow = MutableSharedFlow<List<Product>>()

    private val basketCollection by lazy {
        firestore.collection(Constants.DB_USER).document(auth.uid!!).collection(Constants.DB_BASKET)
    }

    private var basketProductDocuments = emptyList<DocumentSnapshot>()

    override fun signUp(user: User): StateFlow<ResultNet> = flow {
        emit(ResultNet.Loading)

        try {
            val authResult = auth.createUserWithEmailAndPassword(user.mail, user.password).await()
            if(authResult != null && authResult.user != null){
                authResult.user?.let {
                    firestore.collection(Constants.DB_USER).document(it.uid).set(user).await()
                    emit(ResultNet.Success(Unit))
                }
            }
        } catch (e: Exception){
            emit(ResultNet.Error(e.message.toString()))
        }

//            .addOnSuccessListener {
//                it.user?.let {
//                    saveUserInfo(it.uid, user)
//                }
//            }
//            .addOnFailureListener{
//                _signUpState.value = SignUpState.Error(it.message)
//            }

    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = ResultNet.Initial
    )

    override fun getUser(): StateFlow<User> = flow {
        try {
            val userFirebase = firestore.collection(Constants.DB_USER).document(auth.uid!!).get().await()
            val user = userFirebase.toObject(User::class.java)
            user?.let {
                emit(it)
            }
        } catch (e: Exception){
            emit(User())
        }
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = User()
    )

    override fun signIn(email: String, password: String): StateFlow<ResultNet> = flow {
        emit(ResultNet.Loading)

        try {
            val userFirebase = auth.signInWithEmailAndPassword(email,password).await()

            if (userFirebase != null && userFirebase.user != null) emit(ResultNet.Success(Unit))
            else emit(ResultNet.Error("Wrong data"))
        } catch (e: Exception){
            emit(ResultNet.Error(e.message.toString()))
        }

    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = ResultNet.Initial
    )

    override fun signOut() {
        auth.signOut()
    }

    override fun resetPassword(email: String): StateFlow<ResultNet> = flow {
        emit(ResultNet.Loading)

        try {
            auth.sendPasswordResetEmail(email).await()
            emit(ResultNet.Success(Unit))
        } catch (e: Exception){
            emit(ResultNet.Error(e.message.toString()))
        }
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = ResultNet.Initial
    )

    override fun getFavouriteList(phone: String): StateFlow<List<String>> {
        TODO("Not yet implemented")
    }

    private val _basketList = mutableListOf<Product>()

    private val basketList: List<Product>
        get() = _basketList.toList()

    private val loadedListFlow = flow {
        try {
            val collectionFirebase = basketCollection.get().await()
            if (collectionFirebase == null){
                emit(basketList)
            }else {
                basketProductDocuments = collectionFirebase.documents
                val listProduct = collectionFirebase.toObjects(Product::class.java)
                _basketList.addAll(listProduct)
                emit(basketList)
            }
        } catch (e: Exception){
            emit(basketList)
        }
    }.retry {
        delay(RETRY_TIME_OUT)
        true
    }

    private val basket: StateFlow<List<Product>> = loadedListFlow
        .mergeWith(refreshedListFlow)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.Lazily,
            initialValue = basketList
        )

    override fun getBasket(): StateFlow<List<Product>> = basket

    override fun addProductToBasket(product: Product) = flow {
        emit(ResultNet.Loading)
        try {
            val querySnapshot = basketCollection.whereEqualTo(Constants.DB_PRODUCT_ID, product.id).get().await()

            if (querySnapshot.documents.isEmpty()) {
                basketCollection.document().set(product).await()
                emit(ResultNet.Success(Unit))

                _basketList.add(product)
                refreshedListFlow.emit(basketList)
            } else {
                emit(ResultNet.Error("Product already in basket"))
            }
        } catch (e: Exception) {
            emit(ResultNet.Error(e.message.toString()))
        }
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = ResultNet.Initial
    )

    override fun deleteProductFromBasket(product: Product): StateFlow<ResultNet> = flow {
        emit(ResultNet.Loading)
        val index = basketList.indexOf(product)
        if (index != -1) {
            val documentId = basketProductDocuments[index].id
            basketCollection.document(documentId).delete().await()
            _basketList.removeAt(index)

            emit(ResultNet.Success(Unit))
            refreshedListFlow.emit(basketList)
        } else {
            emit(ResultNet.Error("Not found this product"))
        }
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = ResultNet.Initial
    )


    private companion object{
        const val RETRY_TIME_OUT: Long = 5000
    }
}