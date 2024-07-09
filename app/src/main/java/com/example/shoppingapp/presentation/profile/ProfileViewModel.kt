package com.example.shoppingapp.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.domain.entity.User
import com.example.shoppingapp.domain.usecase.GetUserUseCase
import com.example.shoppingapp.domain.usecase.SignOutUseCase
import com.example.shoppingapp.util.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val signOutUseCase: SignOutUseCase
):ViewModel() {

    private val user = getUserUseCase()

    val userState = user
        .filter { it != User() }
        .map { UserState.Success(it) as UserState}
        .onStart { UserState.Loading }


    fun logOut() {
        signOutUseCase()
    }

}