package com.baleshapp.pocketpaper.view

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.baleshapp.pocketpaper.BuildConfig
import com.baleshapp.pocketpaper.R
import com.baleshapp.pocketpaper.databinding.ActivitySettingsBinding
import com.baleshapp.pocketpaper.utils.ShareDataUtil

class SettingsActivity : AppCompatActivity() {

    lateinit var binding: ActivitySettingsBinding
    val versionName = "v" + BuildConfig.VERSION_NAME

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_settings)
        binding.activity = this
        setContentView(binding.root)
    }

    fun sendRecommendation() {
        val appPackageName = packageName
        val shareMessage = resources.getString(R.string.share_recommendation_text)
        val tryAppMessage = resources.getString(R.string.try_app_text)
        ShareDataUtil(
            this,
            "$shareMessage\n$tryAppMessage \n" +
                    "https://play.google.com/store/apps/details?id=$appPackageName"
        )
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