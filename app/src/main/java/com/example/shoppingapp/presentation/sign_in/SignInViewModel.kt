package com.example.shoppingapp.presentation.sign_in

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.presentation.sign_in.dialog.ResetPasswordState
import com.example.shoppingapp.presentation.sign_up.SignUpValidateState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): ViewModel() {

    private val _signInState = MutableStateFlow<SignInState>(SignInState.Initial)
    val signInState = _signInState.asStateFlow()

    private val _validation = Channel<SignInFieldsState>()
    val validateState = _validation.receiveAsFlow()

    private val _resetPassword = MutableSharedFlow<ResetPasswordState>()
    val resetPassword = _resetPassword.asSharedFlow()

    fun signIn(email: String, password: String){
        val emailV = validateEmail(email)
        val passwordV = validatePassword(password)

        if (validateFields(emailV, passwordV)){
            viewModelScope.launch {
                _signInState.emit(SignInState.Loading)
            }

            firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnSuccessListener {
                    viewModelScope.launch {
                        it.user?.let {
                            _signInState.emit(SignInState.Success(it))
                        }
                    }
                }
                .addOnFailureListener{
                    viewModelScope.launch {
                        _signInState.emit(SignInState.Error(it.message.toString()))
                    }
                }
        }else{
            val signInValidateState = SignInFieldsState(
                emailV,
                passwordV
            )

            viewModelScope.launch {
                _validation.send(signInValidateState)
            }
        }

    }

    fun resetPassword(email: String){
        viewModelScope.launch {
            _resetPassword.emit(ResetPasswordState.Loading)
        }
        firebaseAuth
            .sendPasswordResetEmail(email)
            .addOnSuccessListener {
                viewModelScope.launch{
                    _resetPassword.emit(ResetPasswordState.Success(email))
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _resetPassword.emit(ResetPasswordState.Error(it.message))
                }
            }

    }

    private fun validateEmail(email: String): SignInValidateState{
        if (email.isEmpty()) return SignInValidateState.Error("Fill this field")
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return SignInValidateState.Error("Wrong email format")
        return SignInValidateState.Success
    }

    private fun validatePassword(password: String): SignInValidateState{
        if (password.isEmpty()) return SignInValidateState.Error("Fill this field")
        if (password.length < 6)
            return SignInValidateState.Error("Password should contains 6 or more symbols")
        return SignInValidateState.Success
    }
    private fun validateFields(email: SignInValidateState, password: SignInValidateState): Boolean{
        return email is SignInValidateState.Success && password is SignInValidateState.Success
    }


}