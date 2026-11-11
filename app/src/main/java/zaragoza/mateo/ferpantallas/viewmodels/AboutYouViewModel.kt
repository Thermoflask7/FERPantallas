package zaragoza.mateo.ferpantallas.viewmodels

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import zaragoza.mateo.ferpantallas.modelos.EducationLevel
import zaragoza.mateo.ferpantallas.modelos.NoticeCategory
import zaragoza.mateo.ferpantallas.modelos.ScholarshipProfile

class AboutYouViewModel(application: Application) : AndroidViewModel(application) {
    private val preferences = application.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE)

    var welcomeCompleted by mutableStateOf(preferences.contains(PROFILE_KEY))
        private set

    var level by mutableStateOf(readLevel())
        private set

    var profile by mutableStateOf(readProfile())
        private set

    var notices by mutableStateOf(initialNotices())
        private set

    fun saveWelcome(profile: ScholarshipProfile, level: EducationLevel) {
        this.profile = profile
        this.level = level
        preferences.edit()
            .putString(PROFILE_KEY, profile.name)
            .putString(LEVEL_KEY, level.name)
            .apply()
        welcomeCompleted = true
    }

    fun reset() {
        level = EducationLevel.UNIVERSIDAD
        profile = ScholarshipProfile.ASPIRANTE
        notices = initialNotices()
        welcomeCompleted = false
        preferences.edit().remove(PROFILE_KEY).remove(LEVEL_KEY).apply()
    }

    fun onLevelSelected(level: EducationLevel) {
        this.level = level
        if (welcomeCompleted) {
            preferences.edit().putString(LEVEL_KEY, level.name).apply()
        }
    }

    fun onProfileSelected(profile: ScholarshipProfile) {
        this.profile = profile
        if (welcomeCompleted) {
            preferences.edit().putString(PROFILE_KEY, profile.name).apply()
        }
    }

    fun onNoticeToggled(notice: NoticeCategory) {
        notices = if (notice in notices) notices - notice else notices + notice
    }

    private fun readLevel(): EducationLevel {
        val stored = preferences.getString(LEVEL_KEY, null)
        return EducationLevel.entries.firstOrNull { it.name == stored }
            ?: EducationLevel.UNIVERSIDAD
    }

    private fun readProfile(): ScholarshipProfile {
        val stored = preferences.getString(PROFILE_KEY, null)
        return ScholarshipProfile.entries.firstOrNull { it.name == stored }
            ?: ScholarshipProfile.BECADO
    }

    private companion object {
        const val PREFS_FILE = "ajustes"
        const val PROFILE_KEY = "perfil"
        const val LEVEL_KEY = "nivel"

        fun initialNotices(): Set<NoticeCategory> {
            return setOf(
                NoticeCategory.CONVOCATORIAS,
                NoticeCategory.RENOVACION,
                NoticeCategory.EVENTOS_SOCIALES,
                NoticeCategory.ANUNCIOS,
            )
        }
    }
}
