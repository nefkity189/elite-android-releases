package com.elite.sch.app.ui.state

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
    val defaultCameraMode: String = "follow_user", // "follow_user" | "show_full_route"
    val useOfflineMap: Boolean = false // Se true, usa PMTiles local; se false, usa OpenFreeMap online
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

/**
 * Estilos de mapa base (OpenFreeMap, vetorial, sem chave de API).
 * "Escuro" é aplicado pelo botão de modo escuro; os outros três são escolhidos em "Tipo de mapa".
 */
enum class MapBaseStyle(val label: String, val url: String) {
    STANDARD("Padrão", "https://tiles.openfreemap.org/styles/liberty"),
    LIGHT("Claro", "https://tiles.openfreemap.org/styles/positron"),
    CONTRAST("Contraste", "https://tiles.openfreemap.org/styles/bright"),
    DARK("Escuro", "https://tiles.openfreemap.org/styles/dark");

    /** Próximo tipo no botão "Tipo de mapa" (o escuro fica de fora: tem botão próprio). */
    fun nextSelectable(): MapBaseStyle = when (this) {
        STANDARD -> LIGHT
        LIGHT -> CONTRAST
        else -> STANDARD
    }
}

/**
 * Texto do pill do canto inferior esquerdo do mapa, conforme o modo de câmera.
 * O modo inicial vem de Perfil > "Modo de câmera padrão" (Seguir você / Com direção / Rota completa).
 */
fun CameraMode.pillLabel(): String = when (this) {
    CameraMode.FollowUser -> "Seguindo você"
    CameraMode.FollowUserWithBearing -> "Seguindo com direção"
    CameraMode.ShowFullRoute -> "Rota completa"
    CameraMode.Free -> "Mapa livre"
}
