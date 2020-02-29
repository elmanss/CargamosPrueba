package me.elmanss.cargamos.data.local

import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * cgs on 28/02/20.
 */

data class LocalConfig(val baseUrl: String?, val imgSizes: String?)


interface ConfigRepository {
    fun saveConfig(localConfig: LocalConfig)
    fun getConfig(): LocalConfig
    fun isEmpty(): Boolean
}

class ConfigRepositoryImpl(private val prefs: SharedPreferences) : ConfigRepository {

    companion object {
        private const val KEY_SECURE_URL = "me.elmanss.cargamos.secure_url"
        private const val KEY_POSTER_SIZES = "me.elmanss.cargamos.poster_sizes"
    }

    override fun saveConfig(localConfig: LocalConfig) {
        prefs.edit {
            putString(KEY_SECURE_URL, localConfig.baseUrl)
            putString(KEY_POSTER_SIZES, localConfig.imgSizes)
        }
    }

    override fun getConfig(): LocalConfig {
        val baseUrl = prefs.getString(KEY_SECURE_URL, "")
        val imgSizes = prefs.getString(KEY_POSTER_SIZES, "")
        return LocalConfig(baseUrl, imgSizes)
    }

    override fun isEmpty(): Boolean {
        return !prefs.contains(KEY_SECURE_URL) || !prefs.contains(KEY_POSTER_SIZES)
    }

}