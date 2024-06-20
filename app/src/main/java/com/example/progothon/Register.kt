package com.example.progothon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.progothon.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class Register : AppCompatActivity() {
    private lateinit var binding :ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        binding.Btnreg.setOnClickListener {
            val email=binding.Email.text.toString()
            val password=binding.password.text.toString()
            val confirmpassword=binding.confirmpass.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty() && confirmpassword.isNotEmpty()){
                if(password == confirmpassword){
                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                        if (it.isSuccessful){
                            val intent=Intent(this, LOGIN::class.java)
                            startActivity(intent)
                        }
                        else{
                            Toast.makeText(this,it.exception.toString(),Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    Toast.makeText(this,"Password Does not Match",Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this,"Fields Cannot be Empty",Toast.LENGTH_SHORT).show()
            }
        }
        binding.loginredirect.setOnClickListener {
            val loginintent=Intent(this,LOGIN::class.java)
            startActivity(loginintent)
        }
    }
}