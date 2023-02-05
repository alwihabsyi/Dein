package com.alwihabsyi.dein

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.alwihabsyi.dein.databinding.ActivityLoginHomeBinding
import com.alwihabsyi.dein.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginHome : AppCompatActivity() {

    private lateinit var binding: ActivityLoginHomeBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        binding.btnSignin.setOnClickListener {
            SignInFragment().show(supportFragmentManager, "signInFragment")
        }

        binding.btnSignup.setOnClickListener {
            SignUpFragment().show(supportFragmentManager, "signUpFragment")
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(
                Intent(this, MainActivity::class.java)
            )
            finish()
        }
    }
}