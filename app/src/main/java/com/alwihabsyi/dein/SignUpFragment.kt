package com.alwihabsyi.dein

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.alwihabsyi.dein.databinding.FragmentSignUpBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class SignUpFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var gsc: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSignUpBinding.inflate(inflater, container, false)
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

        binding.btnGooglesignin.setOnClickListener {
            googleSignIn()
        }

        binding.btnSignup.setOnClickListener {
            startSigningUp()
        }
    }

    private fun startSigningUp() {
        val etEmail = binding.etemailSU.text
        val etPW = binding.etpasswordSU.text
        val etconfPW = binding.etconfpasswordSU.text
        val etUname = binding.etUserName.text

        val emInput = etEmail.toString()
        val pwInput = etPW.toString()
        val user = etUname.toString()
        val confpwInput = etconfPW.toString()

        if (etEmail!!.isEmpty() || etPW!!.isEmpty() || etconfPW!!.isEmpty() || etUname!!.isEmpty()) {
            Toast.makeText(this.context, R.string.isi_field, Toast.LENGTH_SHORT).show()
            return
        }

        if (confpwInput != pwInput) {
            Toast.makeText(this.context, R.string.pwSalah, Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(emInput, pwInput)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this.context, R.string.suSucceed, Toast.LENGTH_SHORT)
                        .show()

                    database = FirebaseDatabase.getInstance().getReference("User")
                    val uid = auth.currentUser?.uid
                    val datauser = DataUser(uid, user)
                    database.child(uid!!).setValue(datauser)
                } else {
                    Toast.makeText(this.context, "Gagal Mendaftar", Toast.LENGTH_SHORT)
                        .show()
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
        startActivityForResult(signInIntent, SignInFragment.RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == SignInFragment.RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                Log.d(ContentValues.TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            }catch (e: ApiException){
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
            database = FirebaseDatabase.getInstance().getReference("User")
            val datauser = DataUser(user.uid, user.displayName)
            database.child(user.uid).setValue(datauser)
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(SignInFragment.EXTRA_NAME, user.displayName)
            startActivity(intent)
            activity?.finish()
        }
    }
}

