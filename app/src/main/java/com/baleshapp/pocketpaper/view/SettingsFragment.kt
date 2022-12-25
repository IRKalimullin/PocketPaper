package com.baleshapp.pocketpaper.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.baleshapp.pocketpaper.R
import com.baleshapp.pocketpaper.databinding.FragmentSettingsBinding
import com.baleshapp.pocketpaper.utils.settings.AppSettings
import com.baleshapp.pocketpaper.utils.settings.ThemeModes

class SettingsFragment : Fragment() {

    var themeMode = ThemeModes.AUTO
    lateinit var binding: FragmentSettingsBinding
    lateinit var appSettings: AppSettings
    var isThemeChanged = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        binding.fragment = this

        appSettings = AppSettings(binding.root.context)

        themeMode = appSettings.getAppThemeMode()

        setThemeIcon(themeMode)

        return binding.root
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

    private fun setThemeIcon(themeModes: ThemeModes){
        when (themeModes) {
            ThemeModes.AUTO -> binding.appThemeMode.setImageResource(R.drawable.ic_baseline_hdr_auto_24)
            ThemeModes.LIGHT -> binding.appThemeMode.setImageResource(R.drawable.ic_baseline_light_mode_24)
            ThemeModes.DARK -> binding.appThemeMode.setImageResource(R.drawable.ic_baseline_dark_mode_24)
        }
    }
}