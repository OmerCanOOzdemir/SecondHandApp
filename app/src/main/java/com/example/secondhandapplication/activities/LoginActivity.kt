package com.example.secondhandapplication.activities

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import androidx.appcompat.app.AlertDialog
import coil.load
import com.example.secondhandapplication.R
import com.example.secondhandapplication.data.services.Joke
import com.example.secondhandapplication.data.services.JokeApi
import com.google.firebase.auth.FirebaseAuth
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    val url:String ="https://v2.jokeapi.dev/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Get Joke from api
        getJoke()
        //Get Random image from picsum using coil
        findViewById<ImageView>(R.id.login_image).load("https://picsum.photos/200/300")
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

    private fun getJoke() {
        val builder = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(url).build().create(JokeApi::class.java)

        val retrofitData = builder.getRandomJoke()

        retrofitData.enqueue(object: Callback<Joke>{
            override fun onResponse(call: Call<Joke>, response: Response<Joke>) {
                val response = response.body()
                val joke = response!!.joke
                jokeDialog(joke)

            }



            override fun onFailure(call: Call<Joke>, t: Throwable) {
                jokeDialog(getString(R.string.no_joke))
            }

        })

    }
    private fun jokeDialog(joke:String){
        val alertBuilder = AlertDialog.Builder(this)
        alertBuilder.setTitle(getString(R.string.welcome_joke))
        alertBuilder.setMessage(joke)
        alertBuilder.setNeutralButton("ok",{
            DialogInterface:DialogInterface,i:Int ->

        })
        alertBuilder.show()
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
                    val myIntent = Intent(this, MainActivity::class.java)
                    myIntent.putExtra("success",getString(R.string.user_logged_success))
                    this.startActivity(myIntent)
                    finish()
                }else{
                    Toast.makeText(this,getString(R.string.email_password_error), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}

