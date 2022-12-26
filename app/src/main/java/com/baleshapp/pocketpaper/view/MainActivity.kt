package com.baleshapp.pocketpaper.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.baleshapp.pocketpaper.R
import com.baleshapp.pocketpaper.settings.AppSettings
import com.baleshapp.pocketpaper.settings.ThemeModes
import com.baleshapp.pocketpaper.view.calendarpage.CalendarFragment
import com.baleshapp.pocketpaper.view.mainpage.MainPageFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {

    val fragmentManager = supportFragmentManager
    val mainPageFragment = MainPageFragment()
    val calendarPageFragment = CalendarFragment()
    val settingsFragment = SettingsFragment()

    private var themeMode = ThemeModes.AUTO
    private lateinit var appSettings: AppSettings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appSettings = AppSettings(this)

        themeMode = appSettings.getAppThemeMode()

        appSettings.setAppTheme(themeMode)

        setContentView(R.layout.activity_main)

        val navigationView =
            findViewById<BottomNavigationView>(R.id.bottom_navigation_main_activity)

        fragmentManager.beginTransaction().add(R.id.main_container, mainPageFragment).commit()

        navigationView.setOnItemSelectedListener(object :
            NavigationBarView.OnItemReselectedListener,
            NavigationBarView.OnItemSelectedListener {
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

            override fun onNavigationItemReselected(item: MenuItem) {}

        })
    }
}