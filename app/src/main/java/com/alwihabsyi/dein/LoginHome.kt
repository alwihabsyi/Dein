package com.alwihabsyi.dein

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.alwihabsyi.dein.databinding.ActivityLoginHomeBinding
import com.alwihabsyi.dein.databinding.ActivityMainBinding

class LoginHome : AppCompatActivity() {

    private lateinit var binding: ActivityLoginHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignin.setOnClickListener {
            SignInFragment().show(supportFragmentManager, "signInFragment")
        }
    }
}