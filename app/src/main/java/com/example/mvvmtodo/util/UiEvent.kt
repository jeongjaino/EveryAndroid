package com.example.mvvmtodo.util

sealed class UiEvent {

    object PopBackStack: UiEvent()

    data class Navigate(val route: String): UiEvent()

    data class ShowSnackBar(
        val message: String,
        val action: String? = null
    ): UiEvent()
}