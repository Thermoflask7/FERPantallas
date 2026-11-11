package zaragoza.mateo.ferpantallas.viewmodels

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import zaragoza.mateo.ferpantallas.modelos.AppTheme
import zaragoza.mateo.ferpantallas.modelos.TextSize

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val preferences = application.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE)

    var theme by mutableStateOf(readTheme())
        private set

    var textSize by mutableStateOf(readTextSize())
        private set

    fun onThemeSelected(theme: AppTheme) {
        this.theme = theme
        preferences.edit().putString(THEME_KEY, theme.name).apply()
    }

    fun onTextSizeSelected(size: TextSize) {
        textSize = size
        preferences.edit().putString(TEXT_SIZE_KEY, size.name).apply()
    }

    fun clearData() {
        theme = AppTheme.SISTEMA
        textSize = TextSize.NORMAL
        preferences.edit().clear().apply()
    }

    private fun readTheme(): AppTheme {
        val stored = preferences.getString(THEME_KEY, AppTheme.SISTEMA.name)
        return AppTheme.entries.firstOrNull { it.name == stored } ?: AppTheme.SISTEMA
    }

    private fun readTextSize(): TextSize {
        val stored = preferences.getString(TEXT_SIZE_KEY, TextSize.NORMAL.name)
        return TextSize.entries.firstOrNull { it.name == stored } ?: TextSize.NORMAL
    }

    private companion object {
        // AboutYouViewModel stores profile and level in this same file.
        const val PREFS_FILE = "ajustes"
        const val THEME_KEY = "tema"
        const val TEXT_SIZE_KEY = "tamano_texto"
    }
}
