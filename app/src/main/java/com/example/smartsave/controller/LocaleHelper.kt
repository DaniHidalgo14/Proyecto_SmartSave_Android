package com.example.smartsave.controller

import android.content.Context
import android.content.res.Configuration
import android.preference.PreferenceManager
import java.util.Locale

object LocaleHelper {

    private const val PREF_LANGUAGE = "language"

    fun setLocale(context: Context, languageCode: String) {
        saveLanguage(context, languageCode)
        updateResources(context, languageCode)
    }

    fun getSavedLanguage(context: Context): String {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString(PREF_LANGUAGE, "es") ?: "es"
    }

    fun applyLocale(context: Context): Context {
        val language = getSavedLanguage(context)
        return updateResources(context, language)
    }

    private fun saveLanguage(context: Context, languageCode: String) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        prefs.edit().putString(PREF_LANGUAGE, languageCode).apply()
    }

    private fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        return context.createConfigurationContext(config)
    }
}