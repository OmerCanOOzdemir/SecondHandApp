package com.example.secondhandapplication.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.example.secondhandapp.data.user.User
import com.example.secondhandapp.data.user.UserViewModel
import com.example.secondhandapplication.R
import com.example.secondhandapplication.data.address.Address
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth;
    private lateinit var userViewModel: UserViewModel
    private var image_from_galery: Bitmap? = null


    // https://developer.android.com/training/basics/intents/result
    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        //getBitmap is deprecated
        if(Build.VERSION.SDK_INT <28){
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,uri)
            if(bitmap != null){
                image_from_galery = bitmap
            }
        }else{
            val source = uri?.let { ImageDecoder.createSource(this.contentResolver, it) }
            if(source != null){
                image_from_galery = ImageDecoder.decodeBitmap(source)
            }


        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        auth = FirebaseAuth.getInstance()

        val register_btn = findViewById<Button>(R.id.register_button)

        register_btn.setOnClickListener{
            createUserFirebase()
        }
        //upload image
        val upload_image_btn = findViewById<Button>(R.id.upload_profile_image_btn)

        upload_image_btn.setOnClickListener {
            uploadImage()
        }
    }

    private fun uploadImage() {
        getContent.launch("image/*")
    }

    private fun createUserFirebase(){
        val email = findViewById<EditText>(R.id.register_email_input)
        val password = findViewById<EditText>(R.id.register_password_input)
        val firstname = findViewById<EditText>(R.id.register_firstname_input)
        val lastname =  findViewById<EditText>(R.id.register_lastname_input)
        val phone =  findViewById<EditText>(R.id.register_phone_input)
        val street =  findViewById<EditText>(R.id.register_street_input)
        val street_number =  findViewById<EditText>(R.id.register_number_input)
        val upload_image_btn = findViewById<Button>(R.id.upload_profile_image_btn)

        if(TextUtils.isEmpty(email.text.toString())){
            email.error = getString(R.string.email_error)
            email.requestFocus()
        }else if (TextUtils.isEmpty(password.text.toString())){
            password.error = getString(R.string.password_registration_error)
            password.requestFocus()
        }else if(password.text.toString().length < 6){
            password.error = getString(R.string.password_larger_error)
            password.requestFocus()
        }else if(TextUtils.isEmpty(firstname.text.toString())){
            firstname.error = getString(R.string.error_firstname)
            firstname.requestFocus()
        }else if(TextUtils.isEmpty(lastname.text.toString())){
            lastname.error = getString(R.string.lastname_error)
            lastname.requestFocus()
        }
        else if(TextUtils.isEmpty(phone.text.toString())){
            phone.error = getString(R.string.error_phone)
            phone.requestFocus()
        }else if(TextUtils.isEmpty(street.text.toString())){
            street.error = getString(R.string.error_street_name)
            street.requestFocus()
        }else if(TextUtils.isEmpty(street_number.text.toString())){
            street_number.error = getString(R.string.error_street_number)
            street_number.requestFocus()
        }else if (image_from_galery == null){
            upload_image_btn.error = getString(R.string.choose_image_error)
            upload_image_btn.requestFocus()
        }

        else{
            auth.createUserWithEmailAndPassword(email.text.toString(),password.text.toString()).addOnCompleteListener {
                if(it.isSuccessful){
                    val address = Address(street.text.toString(),street_number.text.toString().toInt())
                    val user = User(email.text.toString(),firstname.text.toString(),lastname.text.toString(),phone.text.toString(),
                        image_from_galery!!,address)
                    storeUserInRoom(user)
                    Toast.makeText(this,getString(R.string.success_registration), Toast.LENGTH_LONG).show()
                    val myIntent = Intent(this, LoginActivity::class.java)
                    this.startActivity(myIntent)
                    finish()
                }
                else{
                    Toast.makeText(this,getString(R.string.registration_error) + it.exception, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    private fun storeUserInRoom(user:User){
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.addUser(user)
    }





}