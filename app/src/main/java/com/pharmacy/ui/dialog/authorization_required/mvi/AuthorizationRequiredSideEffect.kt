package com.pharmacy.ui.dialog.authorization_required.mvi

sealed class AuthorizationRequiredSideEffect {
   object OnLoginSuccess : AuthorizationRequiredSideEffect()
}