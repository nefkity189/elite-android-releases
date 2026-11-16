package com.example.ui.state

/**
 * Estado e preferências do mapa em tempo real.
 */

/**
 * Modo de câmera do mapa.
 * - FollowUser: câmera segue o marcador do usuário (centro na posição atual, zoom ~17, bearing = 0)
 * - FollowUserWithBearing: câmera segue o usuário e gira conforme heading do movimento (bearing dinâmico)
 * - ShowFullRoute: câmera enquadra toda a rota (LatLngBounds englobando todos os pontos + padding)
 * - Free: usuário arrasta/zoom manual; botão "Recentralizar" volta para FollowUserWithBearing
 */
sealed interface CameraMode {
    /** Câmera centralizada no usuário com bearing fixo em 0 (norte no topo). */
    data object FollowUser : CameraMode
    /** Câmera centralizada no usuário com bearing dinâmico = heading do movimento. */
    data object FollowUserWithBearing : CameraMode
    /** Câmera enquadra toda a rota (LatLngBounds englobando todos os pontos + padding). */
    data object ShowFullRoute : CameraMode
    /** Usuário arrasta/zoom manual; botão "Recentralizar" volta para FollowUserWithBearing. */
    data object Free : CameraMode
}

/**
 * Preferências de mapa persistidas no perfil do usuário.
 */
data class MapPreferences(
    val showMapDuringRun: Boolean = true,
    val defaultCameraMode: String = "follow_user" // "follow_user" | "show_full_route"
) {
    fun toCameraMode(): CameraMode = when (defaultCameraMode) {
        "show_full_route" -> CameraMode.ShowFullRoute
        "follow_user_with_bearing" -> CameraMode.FollowUserWithBearing
        "follow_user" -> CameraMode.FollowUser
        else -> CameraMode.FollowUser
    }
}

/**
 * Ponto GPS para renderização no mapa.
 */
data class MapPoint(
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float,
    val timestampMillis: Long
)