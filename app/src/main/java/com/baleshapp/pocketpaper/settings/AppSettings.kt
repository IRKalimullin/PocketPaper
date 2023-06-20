package com.baleshapp.pocketpaper.settings

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

const val STORAGE_NAME = "APP_SETTINGS"
const val APP_THEME_KEY = "THEME_MODE"
const val FIRST_START_FLAG = "FIRST_START_FLAG"

/**
 * A class that contains methods for applying settings and working with SharedPreferences
 */
class AppSettings(context: Context) {

    private var settings = context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE)
    private var editor = settings.edit()

    /**
     * Method that get application theme settings from SharedPreferences
     */
    fun getAppThemeMode(): ThemeModes {
        return when (settings.getString(APP_THEME_KEY, ThemeModes.AUTO.name)) {
            "DARK" -> ThemeModes.DARK
            "LIGHT" -> ThemeModes.LIGHT
            else -> ThemeModes.AUTO
        }
    }

    /**
     * Method that saves application theme settings
     */
    fun saveAppThemeMode(themeMode: ThemeModes) {
        editor.putString(APP_THEME_KEY, themeMode.name).apply()
    }

    /**
     * Method that apply app theme mode
     */
    fun setAppTheme(themeMode: ThemeModes) {
        when (themeMode) {
            ThemeModes.LIGHT -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            ThemeModes.DARK -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            else -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.getDefaultNightMode())
            }
        }
    }

    /**
     * Method that gets value of first started app flag
     */
    fun isFirstStart():Boolean  {
        return settings.getBoolean(FIRST_START_FLAG,true)
    }

    /**
     * Method that save first started in preferences
     */
    fun changeFirstStart(){
        editor.putBoolean(FIRST_START_FLAG,false).apply()
    }



}