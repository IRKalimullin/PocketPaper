package com.baleshapp.pocketpaper.view

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.baleshapp.pocketpaper.BuildConfig
import com.baleshapp.pocketpaper.R
import com.baleshapp.pocketpaper.databinding.ActivitySettingsBinding
import com.baleshapp.pocketpaper.settings.AppSettings
import com.baleshapp.pocketpaper.settings.ThemeModes

class SettingsActivity : AppCompatActivity() {

    lateinit var binding: ActivitySettingsBinding
    private var themeMode = ThemeModes.AUTO
    private lateinit var appSettings: AppSettings
    var isThemeChanged = false
    val versionName = "v" + BuildConfig.VERSION_NAME

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val inflater = LayoutInflater.from(this)

        binding = DataBindingUtil.inflate(inflater, R.layout.activity_settings, null, false)
        binding.activity = this

        appSettings = AppSettings(this)

        themeMode = appSettings.getAppThemeMode()

        setThemeIcon(themeMode)

        setContentView(binding.root)
    }

    fun setThemeMode() {
        themeMode = when (themeMode) {
            ThemeModes.AUTO -> {
                ThemeModes.LIGHT
            }
            ThemeModes.LIGHT -> {
                ThemeModes.DARK
            }
            else -> {
                ThemeModes.AUTO
            }
        }
        setThemeIcon(themeMode)
        binding.invalidateAll()
        appSettings.saveAppThemeMode(themeMode)
        isThemeChanged = true
    }

    private fun setThemeIcon(themeModes: ThemeModes) {
        when (themeModes) {
            ThemeModes.AUTO -> binding.appThemeMode.setImageResource(R.drawable.ic_baseline_hdr_auto_24)
            ThemeModes.LIGHT -> binding.appThemeMode.setImageResource(R.drawable.ic_baseline_light_mode_24)
            ThemeModes.DARK -> binding.appThemeMode.setImageResource(R.drawable.ic_baseline_dark_mode_24)
        }
    }

    fun sendRecommendation() {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(
                Intent.EXTRA_TEXT,
                "Я использую приложение Pocket Paper для ведения моего списка дел. \n Попробуй приложение: "
            )
        }
        startActivity(Intent.createChooser(intent, "Поделиться"))
    }

    fun sendErrorReport() {
        val dataInfo = "App information: "
        val appVersion = "app version: " + BuildConfig.VERSION_NAME
        val appBuild = "app build code: " + BuildConfig.VERSION_CODE
        val androidVersion = "android version: " + Build.VERSION.SDK_INT

        val emailChooser = Intent(Intent.ACTION_SENDTO)
        emailChooser.data = Uri.parse("mailto:")
        val email = Intent(Intent.ACTION_SEND)
        email.putExtra(Intent.EXTRA_EMAIL, arrayOf("help.baleshapp@gmail.com"))
        email.putExtra(Intent.EXTRA_SUBJECT, "report/error/pocket-paper")
        email.putExtra(
            Intent.EXTRA_TEXT, "Error message: \n\n\n\n " +
                    "$dataInfo \n\n $appVersion \n $appBuild \n $androidVersion"
        )
        email.selector = emailChooser
        startActivity(email)
    }

    fun rateAppInStore() {
        val appPackageName = packageName
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("market://details?id=$appPackageName")
            }
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
            }
            startActivity(intent)
        }
    }
}