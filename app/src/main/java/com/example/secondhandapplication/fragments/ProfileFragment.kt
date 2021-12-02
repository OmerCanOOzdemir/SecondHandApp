package com.example.secondhandapplication.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.media.Image
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.secondhandapp.data.user.User
import com.example.secondhandapp.data.user.UserViewModel
import com.example.secondhandapplication.R
import com.example.secondhandapplication.activities.LoginActivity
import com.example.secondhandapplication.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.log
import android.content.ContentResolver





class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var userViewModel: UserViewModel
    private lateinit var binding:FragmentProfileBinding
    private lateinit var image_from_galery: Bitmap

    // https://developer.android.com/training/basics/intents/result
    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        val resolver = requireActivity().contentResolver
        //getBitmap is deprecated
        if(Build.VERSION.SDK_INT <28){
            val bitmap = MediaStore.Images.Media.getBitmap(resolver,uri!!)
            image_from_galery = bitmap
            updateImageProfile(image_from_galery)
        }else{
            val source = ImageDecoder.createSource(resolver, uri!!)
            image_from_galery = ImageDecoder.decodeBitmap(source)
            updateImageProfile(image_from_galery)
        }
    }

    @SuppressLint("SetTextI18n", "NewApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileBinding.inflate(layoutInflater)
        val view = binding.root


        //Inflate menu
        val toolbar = binding.actionbarProfile
        toolbar.inflateMenu(R.menu.menu_profile)


        //Action for menu
        toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.logout ->{
                    //Logout
                    auth.signOut()
                    val current_user = auth.currentUser
                    if(current_user == null) {
                        val myIntent = Intent(context, LoginActivity::class.java)
                        this.startActivity(myIntent)

                    }
                    true
                    }
                R.id.edit_profile -> {
                    Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_editProfileFragment)
                    true
                }else -> false
                }

            }
        //Get firebaseAuth instance
        auth = FirebaseAuth.getInstance()
        //Instance of user view model
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        //set User informations
        setInformations(view)
        //Edit password or mail
        val edit_password_or_mail_btn = view.findViewById<Button>(R.id.change_password_or_mail_button)
        edit_password_or_mail_btn.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_editEmailPasswordFragment)
        }

        //Edit Image
        val edit_image_btn = view.findViewById<Button>(R.id.change_image_button)
        edit_image_btn.setOnClickListener {
            uploadImage()
        }
        //inflate view
        return view
    }




    private fun setInformations(view:View){
        userViewModel.getAuthUser(auth.currentUser!!.email!!).observe(viewLifecycleOwner,
            Observer {
                val user = it
                val name = view.findViewById<TextView>(R.id.name_profile)
                val email = view.findViewById<TextView>(R.id.email_profile)
                val phone =  view.findViewById<TextView>(R.id.phone_profile)
                val address =  view.findViewById<TextView>(R.id.address_profile)
                val image = view.findViewById<ImageView>(R.id.image_profile)

                name.setText(user.firstname + " " + user.lastname)
                email.setText(user.email)
                phone.setText(user.phone)
                address.setText(user.address.streetname + user.address.streetnumber)
                image.setImageBitmap(user.image)
            })

    }

    private fun uploadImage() {
        getContent.launch("image/*")
    }
    private fun updateImageProfile(image:Bitmap){
        userViewModel.updateImageUser(auth.currentUser!!.email!!,image)
        setInformations(requireView())
    }

}