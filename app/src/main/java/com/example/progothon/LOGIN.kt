package com.example.progothon

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.progothon.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LOGIN : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE)


        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.Btnlog.setOnClickListener {
            val email = binding.Email.text.toString()
            val password = binding.password.text.toString()
            val adminemail:String="admin1@gmail.com"

            if (email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        // Save the login state
                        saveLoginState()
                        if(email.toString()==adminemail){
                            val intent = Intent(this, Evntcrt::class.java)
                            startActivity(intent)
                            finish()
                        }
                        else{
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish() }
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Fields Cannot be Empty", Toast.LENGTH_SHORT).show()
            }
        }

        binding.signup.setOnClickListener {
            val signupintent = Intent(this, Register::class.java)
            startActivity(signupintent)
        }
    }

    private fun saveLoginState() {
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", true)
        editor.apply()
    }
}
