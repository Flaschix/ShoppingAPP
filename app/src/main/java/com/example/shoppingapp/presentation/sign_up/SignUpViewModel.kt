package com.example.shoppingapp.presentation.sign_up

import androidx.lifecycle.ViewModel
import com.example.shoppingapp.domain.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@HiltViewModel
class SignUpViewModel(
    private val auth: FirebaseAuth
):ViewModel() {

    var _signUpState = MutableStateFlow<SignUpState>(SignUpState.Loading)
    val signUpState: Flow<SignUpState> = _signUpState

    fun signUpUser(user: User){
        auth.createUserWithEmailAndPassword(user.mail, user.password)
            .addOnSuccessListener {
                it.user?.let {
                    _signUpState.value = SignUpState.Success(it)
                }
            }
            .addOnFailureListener{
                _signUpState.value = SignUpState.Error(it.message)
            }
    }
}