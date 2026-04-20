package com.example.carelyze.data.local

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )
    
    companion object {
        private const val PREFS_NAME = "CarelyzePrefs"
        private const val KEY_ONBOARDING_SHOWN = "onboarding_shown"
        private const val KEY_ACCESS_TOKEN = "access_token"
        
        @Volatile
        private var INSTANCE: PreferencesManager? = null
        
        fun getInstance(context: Context): PreferencesManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: PreferencesManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
    
    fun isOnboardingShown(): Boolean {
        return prefs.getBoolean(KEY_ONBOARDING_SHOWN, false)
    }
    
    fun setOnboardingShown(shown: Boolean) {
        prefs.edit().putBoolean(KEY_ONBOARDING_SHOWN, shown).apply()
    }

    fun getAccessToken(): String? {
        return prefs.getString(KEY_ACCESS_TOKEN, null)
    }

    fun setAccessToken(token: String) {
        prefs.edit().putString(KEY_ACCESS_TOKEN, token).apply()
    }

    fun clearAccessToken() {
        prefs.edit().remove(KEY_ACCESS_TOKEN).apply()
    }
}
