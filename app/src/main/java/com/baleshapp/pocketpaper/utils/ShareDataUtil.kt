package com.baleshapp.pocketpaper.utils

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.baleshapp.pocketpaper.R

class ShareDataUtil(
    private val activity: AppCompatActivity,
    private val text: String
) {

    init {
        send()
    }

    private fun send() {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(
                Intent.EXTRA_TEXT,
                text
            )
        }
        val title = activity.resources.getString(R.string.share)
        activity.startActivity(Intent.createChooser(intent, title))
    }
}