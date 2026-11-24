package com.elite.sch.app.account

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

interface AccountRepository {
    val authState: StateFlow<AuthState>
    suspend fun signIn(login: String, password: String)
    suspend fun signInWithGoogle(idToken: String)
    suspend fun signOut()
    suspend fun deleteAccount()
    suspend fun updateUf(uf: String)
}

class FirebaseAccountRepository(private val store: AccountStore) : AccountRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val mutableState = MutableStateFlow<AuthState>(AuthState.SignedOut)
    override val authState: StateFlow<AuthState> = mutableState.asStateFlow()

    suspend fun restore() {
        val user = firebaseAuth.currentUser
        if (user == null) {
            store.save(null)
            mutableState.value = AuthState.SignedOut
            return
        }
        mutableState.value = AuthState.SigningIn
        runCatching { persistFirebaseUser(user) }
            .onSuccess { account ->
                store.save(account)
                mutableState.value = AuthState.SignedIn(account)
            }
            .onFailure { error ->
                mutableState.value = AuthState.Error("Sessão Firebase ativa, mas falhou a sincronização do perfil: ${error.message}")
            }
    }

    override suspend fun signIn(login: String, password: String) {
        mutableState.value = AuthState.SigningIn
        try {
            val result = firebaseAuth.signInWithEmailAndPassword(login.trim(), password).awaitResult()
            val user = result.user ?: error("O Firebase não retornou o usuário.")
            val account = persistFirebaseUser(user)
            store.save(account)
            mutableState.value = AuthState.SignedIn(account)
        } catch (error: Exception) {
            mutableState.value = AuthState.Error(error.message ?: "Login ou senha incorretos.")
        }
    }

    override suspend fun signInWithGoogle(idToken: String) {
        mutableState.value = AuthState.SigningIn
        try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = firebaseAuth.signInWithCredential(credential).awaitResult()
            val user = result.user ?: error("O Firebase não retornou o usuário Google.")
            val account = persistFirebaseUser(user)
            store.save(account)
            mutableState.value = AuthState.SignedIn(account)
        } catch (error: Exception) {
            mutableState.value = AuthState.Error(error.message ?: "Não foi possível entrar com Google.")
        }
    }

    override suspend fun signOut() { firebaseAuth.signOut(); store.save(null); mutableState.value = AuthState.SignedOut }
    override suspend fun deleteAccount() = signOut()
    override suspend fun updateUf(uf: String) {
        if (uf !in BRAZIL_UFS) return
        val current = (mutableState.value as? AuthState.SignedIn)?.account ?: return
        val updated = current.copy(uf = uf)
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null && firebaseUser.uid == current.uid) {
            firestore.collection("users").document(current.uid)
                .update("state", uf)
                .awaitResult()
        }
        store.save(updated)
        mutableState.value = AuthState.SignedIn(updated)
    }

    private suspend fun persistFirebaseUser(user: FirebaseUser): UserAccount {
        val ref = firestore.collection("users").document(user.uid)
        val displayName = user.displayName?.takeIf { it.isNotBlank() } ?: "Atleta Elite"
        val email = user.email ?: ""
        val season = firestore.collection("config").document("season").get().awaitResult()
        val activeSeasonId = season.getString("id") ?: season.getLong("year")?.toString()
            ?: error("A temporada ativa não está configurada no Firebase.")
        val existing = ref.get().awaitResult()
        val storedUf = existing.getString("state")
        if (existing.exists()) {
            if (existing.getString("seasonId") == null) {
                ref.update("seasonId", activeSeasonId).awaitResult()
            }
            ref.update(
                mapOf(
                    "name" to displayName,
                    "avatarSeed" to user.uid.take(12)
                )
            ).awaitResult()
        } else {
            ref.set(
                mapOf(
                    "name" to displayName,
                    "state" to "",
                    "avatarSeed" to user.uid.take(12),
                    "createdAt" to FieldValue.serverTimestamp(),
                    "isPremium" to false,
                    "elo" to 0,
                    "elo25" to 0,
                    "elo5" to 0,
                    "elo10" to 0,
                    "seasonId" to activeSeasonId
                ),
                SetOptions.merge()
            ).awaitResult()
        }
        return UserAccount(user.uid, displayName, email, user.photoUrl?.toString(), storedUf)
    }
}

private suspend fun <T> Task<T>.awaitResult(): T = suspendCancellableCoroutine { continuation ->
    addOnSuccessListener { result -> continuation.resume(result) }
    addOnFailureListener { error -> continuation.resumeWithException(error) }
    addOnCanceledListener { continuation.cancel() }
}
