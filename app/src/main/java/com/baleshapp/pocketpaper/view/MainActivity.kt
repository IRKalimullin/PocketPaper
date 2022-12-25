package com.baleshapp.pocketpaper.view

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.baleshapp.pocketpaper.R
import com.baleshapp.pocketpaper.utils.settings.AppSettings
import com.baleshapp.pocketpaper.utils.settings.ThemeModes
import com.baleshapp.pocketpaper.view.calendarpage.CalendarFragment
import com.baleshapp.pocketpaper.view.mainpage.MainPageFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    val fragmentManager = supportFragmentManager
    val mainPageFragment = MainPageFragment()
    val calendarPageFragment = CalendarFragment()
    val settingsFragment = SettingsFragment()

    var themeMode = ThemeModes.AUTO
    lateinit var appSettings: AppSettings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appSettings = AppSettings(this)

        themeMode = appSettings.getAppThemeMode()

        appSettings.setAppTheme(themeMode)

        setContentView(R.layout.activity_main)

        val navigationView =
            findViewById<BottomNavigationView>(R.id.bottom_navigation_main_activity)


        fragmentManager.beginTransaction().add(R.id.main_container, mainPageFragment).commit()

        navigationView.setOnNavigationItemSelectedListener(object :
            BottomNavigationView.OnNavigationItemReselectedListener,
            BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.nav_menu_main_page -> fragmentManager.beginTransaction()
                        .replace(R.id.main_container, mainPageFragment).commit()

                    R.id.nav_menu_calendar_page -> fragmentManager.beginTransaction()
                        .replace(R.id.main_container, calendarPageFragment).commit()

                    R.id.nav_menu_settings_page -> fragmentManager.beginTransaction()
                        .replace(R.id.main_container, settingsFragment).commit()
                }
                return true
            }

            override fun onNavigationItemReselected(item: MenuItem) {

            }

        })
    }



}