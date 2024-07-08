package com.example.shoppingapp.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.domain.entity.User
import com.example.shoppingapp.util.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
):ViewModel() {

    private val _user = MutableStateFlow<UserState>(UserState.Initial)

    val user = _user.asStateFlow()

    init {
        getUser()
    }

    private fun getUser(){
        viewModelScope.launch { _user.emit(UserState.Loading) }

        firestore.collection(Constants.DB_USER).document(auth.uid!!).get()
            .addOnSuccessListener {
                val user = it.toObject(User::class.java)
                user?.let {
                    viewModelScope.launch {
                        _user.emit(UserState.Success(it))
                    }
                }
            }
            .addOnFailureListener {
                viewModelScope.launch{
                    _user.emit(UserState.Error(it.message.toString()))
                }
            }
    }

    fun logOut() {
        auth.signOut()
    }

}