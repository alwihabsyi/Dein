package com.alwihabsyi.dein

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class FirstTimeSplash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_time_splash)

        findViewById<Button>(R.id.btn_continue).apply {
            setOnClickListener {
                startActivity(Intent(this@FirstTimeSplash, LoginHome::class.java))
                finish()
            }
        }
    }
}