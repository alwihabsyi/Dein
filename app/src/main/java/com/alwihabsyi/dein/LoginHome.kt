package com.alwihabsyi.dein

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alwihabsyi.dein.databinding.ActivityLoginHomeBinding
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


        //Button Action
        binding.btnSignin.setOnClickListener {
            SignInFragment().show(supportFragmentManager, "signInFragment")
        }

        binding.btnSignup.setOnClickListener {
            SignUpFragment().show(supportFragmentManager, "signUpFragment")
        }

        //Button and Logo Animation
//        binding.logo.scaleX = 0f
//        binding.logo.scaleY = 0f
//        binding.logo.alpha = 0f
        binding.btnSignin.scaleX = 0f
        binding.btnSignin.scaleY = 0f
        binding.btnSignup.scaleX = 0f
        binding.btnSignup.scaleY = 0f
        binding.btnSignin.alpha = 0f
        binding.btnSignup.alpha = 0f

//        binding.logo.animate().apply {
//            duration = 400
//            scaleY(1.2f)
//            scaleX(1.2f)
//            alpha(0.5f)
//        }.withEndAction {
//            binding.logo.animate().setDuration(300).alpha(1f).scaleX(1f).scaleY(1f)
//        }

        binding.btnSignup.animate().apply {
            duration = 400
            scaleY(1.2f)
            scaleX(1.2f)
            alpha(0.5f)
        }.withEndAction {
            binding.btnSignup.animate().setDuration(300).alpha(1f).scaleX(1f).scaleY(1f)
        }

        binding.btnSignin.animate().apply {
            duration = 400
            scaleY(1.2f)
            scaleX(1.2f)
            alpha(0.5f)
        }.withEndAction {
            binding.btnSignin.animate().setDuration(300).alpha(1f).scaleX(1f).scaleY(1f)
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