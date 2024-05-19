package com.example.shoppingapp.presentation.sign_up

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.domain.entity.User
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val auth: FirebaseAuth
):ViewModel() {

    private var _signUpState = MutableStateFlow<SignUpState>(SignUpState.Initial)
    val signUpState: Flow<SignUpState> = _signUpState

    private val _validation = Channel<SignUpFieldsState>()
    val validateState = _validation.receiveAsFlow()

    fun signUpUser(user: User){
        if (validateFields(user)){
            viewModelScope.launch {
                _signUpState.emit(SignUpState.Loading)
            }

            auth.createUserWithEmailAndPassword(user.mail, user.password)
                .addOnSuccessListener {
                    it.user?.let {
                        _signUpState.value = SignUpState.Success(it)
                    }
                }
                .addOnFailureListener{
                    _signUpState.value = SignUpState.Error(it.message)
                }
        } else{
            val fieldsState = SignUpFieldsState(
                validateName(user.name),
                validateName(user.surname),
                validateEmail(user.mail),
                validatePassword(user.password)
            )

            viewModelScope.launch {
                _validation.send(fieldsState)
            }
        }


    }


    private fun validateName(name: String): SignUpValidateState{
        if(name.isEmpty()) return SignUpValidateState.Error("Fill this field")

        return SignUpValidateState.Success
    }

    private fun validateEmail(email: String): SignUpValidateState{
        if(email.isEmpty()) return SignUpValidateState.Error("Fill this field")
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return SignUpValidateState.Error("Wrong email format")

        return SignUpValidateState.Success
    }

    private fun validatePassword(password: String): SignUpValidateState{
        if(password.isEmpty()) return SignUpValidateState.Error("Fill this field")
        if(password.length < 6)
            return SignUpValidateState.Error("Password should contains 6 or more symbols")

        return SignUpValidateState.Success
    }

    private fun validateFields(user: User): Boolean{
        val nameV = validateName(user.name)
        val surnameV = validateName(user.surname)
        val emailV = validateEmail(user.mail)
        val passwordV = validatePassword(user.password)

        return emailV is SignUpValidateState.Success && passwordV is SignUpValidateState.Success &&
                nameV is SignUpValidateState.Success && surnameV is SignUpValidateState.Success
    }
}