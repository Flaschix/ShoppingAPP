package com.example.shoppingapp.presentation.start_screen

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartScreenViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): ViewModel() {

    private val _navState = MutableStateFlow(NOT_AUTH)

    val navState: StateFlow<Int> = _navState.asStateFlow()

    init {
        val user = firebaseAuth.currentUser

        if (user != null){
            viewModelScope.launch {
                _navState.emit(AUTHED)
            }
        }
    }

    companion object {
        const val NOT_AUTH = 0
        const val AUTHED = 100
    }
}