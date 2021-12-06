package com.example.secondhandapplication.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.secondhandapp.data.user.User
import com.example.secondhandapp.data.user.UserViewModel
import com.example.secondhandapplication.R
import com.example.secondhandapplication.activities.EditProductActivity
import com.example.secondhandapplication.activities.LoginActivity
import com.example.secondhandapplication.data.address.Address
import com.example.secondhandapplication.data.product.ProductViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class EditProfileFragment : Fragment() {

    private lateinit var auth:FirebaseAuth
    private lateinit var userViewModel: UserViewModel
    private lateinit var auth_user : FirebaseUser
    private  var image:Bitmap? = null
    private lateinit var productViewModel: ProductViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)

        //Get auth user
        auth = FirebaseAuth.getInstance()
        auth_user = auth.currentUser!!

        //Set input field
        setUserInformations(view)
        //Delete action
        val delete_btn = view.findViewById<Button>(R.id.delete_profile_button)

        delete_btn.setOnClickListener {
            deleteUserFirebase(view)
        }

        //Edit action
        val edit_btn = view.findViewById<Button>(R.id.edit_profile_button)

        edit_btn.setOnClickListener {
            updateUser(view)
        }

        return view
    }


    private fun deleteUserFirebase(view: View){

        val password = view.findViewById<EditText>(R.id.edit_password_input)
        if(TextUtils.isEmpty(password.text)){
            password.error = getString(R.string.delete_error)
        }else{
            //Re authenticate user
            val email = auth_user.email
            val credential = EmailAuthProvider.getCredential(email!!, password.text.toString())

            auth_user.reauthenticate(credential).addOnCompleteListener {
                auth_user.delete().addOnCompleteListener {
                    if(it.isSuccessful){
                        auth.signOut()
                        val myIntent = Intent(context, LoginActivity::class.java)
                        this.startActivity(myIntent)
                        Toast.makeText(context,getString(R.string.user_deleted),Toast.LENGTH_SHORT).show()
                        deleteUserRoom(email)

                    }else{
                        Toast.makeText(context,getString(R.string.delete_firebase_error) + it.exception,Toast.LENGTH_SHORT).show()
                    }
                }
            }

            }
        }
    private fun deleteUserRoom(email:String){
        userViewModel.deleteByEmailUser(email)
        productViewModel.deleteAllUserProduct(email)
    }
    private fun setUserInformations(view: View){
        //Get room user
        userViewModel.getUserByEmail(auth.currentUser!!.email!!).observe(viewLifecycleOwner,
            Observer{
                val user = it
                //Get all fields
                image = it.image
                val firstname = view.findViewById<EditText>(R.id.edit_firstname_input)
                val lastname = view.findViewById<EditText>(R.id.edit_lastname_input)
                val phone = view.findViewById<EditText>(R.id.edit_phone_input)
                val street_name = view.findViewById<EditText>(R.id.edit_street_input)
                val street_number = view.findViewById<EditText>(R.id.edit_number_input)

                //Set fields value
                firstname.setText(user.firstname)
                lastname.setText(user.lastname)
                phone.setText(user.phone)
                street_name.setText(user.address.streetname)
                street_number.setText(user.address.streetnumber.toString())
            })


    }
    private fun updateUser(view:View){
        //Get all fields
        val firstname = view.findViewById<EditText>(R.id.edit_firstname_input)
        val lastname = view.findViewById<EditText>(R.id.edit_lastname_input)
        val phone = view.findViewById<EditText>(R.id.edit_phone_input)
        val street_name = view.findViewById<EditText>(R.id.edit_street_input)
        val street_number = view.findViewById<EditText>(R.id.edit_number_input)
        val address = Address(street_name.text.toString(),street_number.text.toString().toInt())

        // Update user
        val user_with_new_informations= User(auth_user.email!!,firstname.text.toString(),lastname.text.toString(),phone.text.toString(),
            image!!,address)
        userViewModel.updateUser(user_with_new_informations)

        //Go to profile fragment
        Navigation.findNavController(view).navigate(R.id.action_editProfileFragment_to_profileFragment)
        Toast.makeText(context,getString(R.string.user_updated),Toast.LENGTH_SHORT).show()

    }

}