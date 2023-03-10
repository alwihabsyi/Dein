package com.alwihabsyi.dein

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.alwihabsyi.dein.databinding.FragmentSignInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Suppress("UNREACHABLE_CODE")
class SignInFragment : BottomSheetDialogFragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentSignInBinding
    private lateinit var gsc: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
        auth = Firebase.auth
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("793143099222-j5r05tbkdqilsken13gv4f9jj3u3s5bj.apps.googleusercontent.com")
            .requestEmail()
            .build()
        gsc = GoogleSignIn.getClient(requireContext(), gso)

        binding.btnSignin.setOnClickListener {
            startSigningIn()
        }

        binding.btnGooglesignin.setOnClickListener {
            googleSignIn()
        }
    }

    private fun startSigningIn() {
        val email = binding.etEmail.text
        val password = binding.etPassword.text
        val emInput = email.toString()
        val pwInput = password.toString()

        if(email!!.isEmpty() || password!!.isEmpty()){
            Toast.makeText(this.context, R.string.isi_field, Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(emInput, pwInput).addOnCompleteListener {
            if (it.isSuccessful) {
                val intent = Intent(this.context, MainActivity::class.java)
                startActivity(intent)
                activity?.finish()
            } else {
                Toast.makeText(this.context, "", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this.context, it.localizedMessage, Toast.LENGTH_SHORT)
                .show()
        }
    }

    companion object{
        const val RC_SIGN_IN = 1000
        const val EXTRA_NAME = "EXTRA NAME"
    }

    private fun googleSignIn() {
        val signInIntent = gsc.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                Log.d(ContentValues.TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            }catch (e:ApiException){
                Log.w(ContentValues.TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String){
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    Log.d(ContentValues.TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                }else{
                    Log.w(ContentValues.TAG, "signInWithCredential:failed" + it.exception)
                    updateUI(null)
                }
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if(user != null){
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(EXTRA_NAME, user?.displayName)
            startActivity(intent)
            activity?.finish()
        }
    }
}