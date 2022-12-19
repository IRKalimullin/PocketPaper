package com.baleshapp.pocketpaper.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.baleshapp.pocketpaper.R
import com.baleshapp.pocketpaper.view.mainpage.MainPageFragment
import com.baleshapp.pocketpaper.view.note.NoteListFragment
import com.baleshapp.pocketpaper.view.purchase.PurchaseFragment
import com.baleshapp.pocketpaper.view.task.CurrentTasksFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentTasksFragment = CurrentTasksFragment()
        val notesListFragment = NoteListFragment()
        val purchaseFragment = PurchaseFragment()

        //New fragments
        val mainPageFragment = MainPageFragment()

        val navigationView =
            findViewById<BottomNavigationView>(R.id.bottom_navigation_main_activity)

        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().add(R.id.main_container, mainPageFragment).commit()

        navigationView.setOnNavigationItemSelectedListener(object :
            BottomNavigationView.OnNavigationItemReselectedListener,
            BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.nav_menu_main_page -> fragmentManager.beginTransaction()
                        .replace(R.id.main_container, mainPageFragment).commit()

                }
                return true
            }

            override fun onNavigationItemReselected(item: MenuItem) {

            }

        })
    }
}