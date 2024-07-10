package com.example.shoppingapp.presentation.start_screen

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.domain.entity.ResultNet
import com.example.shoppingapp.domain.usecase.CheckAuthStateUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartScreenViewModel @Inject constructor(
    private val checkAuthStateUseCase: CheckAuthStateUseCase
): ViewModel() {

    private val _navState = MutableStateFlow(NOT_AUTH)

    val navState: StateFlow<Int> = _navState.asStateFlow()

    private fun getAuth(){
        viewModelScope.launch {
            checkAuthStateUseCase().collect{
                if (it) _navState.emit(AUTHED)
            }
        }
    }

    init {
        getAuth()
    }

    companion object {
        const val NOT_AUTH = 0
        const val AUTHED = 100
    }
}