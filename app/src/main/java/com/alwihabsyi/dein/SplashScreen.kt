package com.alwihabsyi.dein

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val preferences = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
        val firstTime = preferences.getString("FirstTimeInstall", "yes")

        if (firstTime.equals("yes")) {
            startActivity(Intent(this@SplashScreen, FirstTimeSplash::class.java))
            finish()
        }

        val editor = preferences.edit()
        editor.putString("FirstTimeInstall", "no")
        editor.apply()

        val splashLogo = findViewById<ImageView>(R.id.logosplash)
        splashLogo.alpha = 0f

        splashLogo.animate().apply {
            duration = 800
            alpha(1f)
        }.withEndAction {
            startActivity(Intent(this@SplashScreen, LoginHome::class.java))
            finish()
        }
    }
}