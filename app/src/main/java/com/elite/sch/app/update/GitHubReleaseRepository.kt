package com.elite.sch.app.update

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

data class AppRelease(
    val versionName: String,
    val apkUrl: String,
    val pageUrl: String,
    val notes: String,
)

object GitHubReleaseRepository {
    private const val API_URL =
        "https://api.github.com/repos/nefkity189/elite-android-releases/releases/latest"

    suspend fun checkForUpdate(context: Context): AppRelease? = withContext(Dispatchers.IO) {
        val connection = (URL(API_URL).openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            connectTimeout = 8_000
            readTimeout = 8_000
            useCaches = false
            setRequestProperty("Accept", "application/vnd.github+json")
            setRequestProperty("X-GitHub-Api-Version", "2022-11-28")
            setRequestProperty("User-Agent", "ELITE-Android-App")
        }

        try {
            if (connection.responseCode !in 200..299) return@withContext null
            val body = connection.inputStream.bufferedReader().use { it.readText() }
            val release = JSONObject(body)
            val tag = release.optString("tag_name").removePrefix("v")
            val currentVersion = context.packageManager
                .getPackageInfo(context.packageName, 0).versionName.orEmpty()
            val apk = release.optJSONArray("assets")
                ?.let { assets -> (0 until assets.length()).map(assets::getJSONObject) }
                ?.firstOrNull { it.optString("name").endsWith(".apk", ignoreCase = true) }
                ?.optString("browser_download_url")
                .orEmpty()

            if (!isNewerVersion(tag, currentVersion) || apk.isBlank()) return@withContext null

            AppRelease(
                versionName = tag,
                apkUrl = apk,
                pageUrl = release.optString("html_url"),
                notes = release.optString("body").take(700),
            )
        } finally {
            connection.disconnect()
        }
    }

    private fun isNewerVersion(latest: String, current: String): Boolean {
        fun parts(value: String): List<Int>? {
            val normalized = value.substringBefore('-').substringBefore('+')
            if (normalized.isBlank() || normalized.split('.').any { it.toIntOrNull() == null }) return null
            return normalized.split('.').map { it.toInt() }
        }

        val latestParts = parts(latest) ?: return false
        val currentParts = parts(current) ?: return false
        repeat(maxOf(latestParts.size, currentParts.size)) { index ->
            val latestPart = latestParts.getOrElse(index) { 0 }
            val currentPart = currentParts.getOrElse(index) { 0 }
            if (latestPart != currentPart) return latestPart > currentPart
        }
        return false
    }
}
