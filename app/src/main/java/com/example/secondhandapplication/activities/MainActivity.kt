package com.example.secondhandapplication.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController

import com.example.secondhandapplication.R
import com.example.secondhandapplication.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.ui.NavigationUI
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(this, R.id.fragment)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navbar)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)


    }
    override fun onStart() {
        super.onStart()
        auth = FirebaseAuth.getInstance()
        //auth.signOut()
        val user = auth.currentUser
        if(user == null){
            val myIntent = Intent(this, LoginActivity::class.java)
            this.startActivity(myIntent)
        }

    }
}