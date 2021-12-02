package com.example.secondhandapplication.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.secondhandapplication.R
import com.example.secondhandapplication.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        val register_btn = findViewById<TextView>(R.id.register_button)
        val login_btn = findViewById<Button>(R.id.login_button)

        register_btn.setOnClickListener{
            val myIntent = Intent(this, RegisterActivity::class.java)
            this.startActivity(myIntent)
            finish()
        }
        login_btn.setOnClickListener{
            loginUser()
        }
    }
    private fun loginUser(){
        val email = findViewById<EditText>(R.id.login_email_input)
        val password = findViewById<EditText>(R.id.login_password_input)

        if(TextUtils.isEmpty(email.text.toString())){
            email.error = getString(R.string.email_error)
            email.requestFocus()

        }else if (TextUtils.isEmpty(password.text.toString())){
            password.error = getString(R.string.password_registration_error)
            password.requestFocus()
        }else{
            auth.signInWithEmailAndPassword(email.text.toString(),password.text.toString()).addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(this,getString(R.string.user_logged_success), Toast.LENGTH_SHORT).show()
                    val myIntent = Intent(this, MainActivity::class.java)
                    this.startActivity(myIntent)
                }else{
                    Toast.makeText(this,getString(R.string.email_password_error), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}