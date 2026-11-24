package com.elite.sch.app.account

data class UserAccount(
    val uid: String,
    val displayName: String,
    val email: String,
    val photoUrl: String? = null,
    val uf: String? = null
)

sealed interface AuthState {
    data object SignedOut : AuthState
    data object SigningIn : AuthState
    data class Error(val message: String) : AuthState
    data class SignedIn(val account: UserAccount) : AuthState
}

enum class AccountDialog { None, SignOut, Delete, UfSheet }

val BRAZIL_UFS = listOf("AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO")
