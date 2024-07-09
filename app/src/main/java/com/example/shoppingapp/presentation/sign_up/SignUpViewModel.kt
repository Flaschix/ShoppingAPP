package com.example.shoppingapp.presentation.sign_up

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.domain.entity.ResultNet
import com.example.shoppingapp.domain.entity.User
import com.example.shoppingapp.domain.usecase.SignUpUseCase
import com.example.shoppingapp.util.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
):ViewModel() {

    private val _signUpState = MutableStateFlow<SignUpState>(SignUpState.Initial)
    val signUpState: Flow<SignUpState> = _signUpState.asStateFlow()

    private val _validation = Channel<SignUpFieldsState>()
    val validateState = _validation.receiveAsFlow()

    fun signUpUser(user: User){
        if (validateFields(user)){
            viewModelScope.launch {
                signUpUseCase(user).collect{
                    when(it){
                        is ResultNet.Error -> _signUpState.value = SignUpState.Error(it.message)
                        ResultNet.Initial -> {}
                        ResultNet.Loading -> _signUpState.emit(SignUpState.Loading)
                        is ResultNet.Success<*> -> _signUpState.value = SignUpState.Success
                    }
                }
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