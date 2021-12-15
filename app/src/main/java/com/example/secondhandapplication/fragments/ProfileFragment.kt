package com.example.secondhandapplication.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.secondhandapp.data.user.UserViewModel
import com.example.secondhandapplication.R
import com.example.secondhandapplication.activities.LoginActivity
import com.example.secondhandapplication.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.secondhandapp.entities.RecyclerViewAdapter
import com.example.secondhandapplication.shared.SharedViewModel


class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var userViewModel: UserViewModel
    private lateinit var binding:FragmentProfileBinding
    private lateinit var image_from_galery: Bitmap
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var adapter: RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
    private val sharedViewModel : SharedViewModel by activityViewModels()


    // https://developer.android.com/training/basics/intents/result
    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        val resolver = requireActivity().contentResolver
        //getBitmap is deprecated
        if(Build.VERSION.SDK_INT <28){
            val bitmap = MediaStore.Images.Media.getBitmap(resolver,uri)
            if(bitmap!=null){
                image_from_galery = bitmap
                updateImageProfile(image_from_galery)
            }

        }else{
            val source = uri?.let { ImageDecoder.createSource(resolver, it) }
            if(source != null){
                image_from_galery = ImageDecoder.decodeBitmap(source)
                updateImageProfile(image_from_galery)
            }

        }
    }

    @SuppressLint("SetTextI18n", "NewApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileBinding.inflate(layoutInflater)
        val view = binding.root

        //Get firebaseAuth instance
        auth = FirebaseAuth.getInstance()
        //Instance of user view model
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        //set User informations
        setInformations(view)
        //Inflate menu
        val toolbar = binding.actionbarProfile
        toolbar.inflateMenu(R.menu.menu_profile)

        //Recyclerview setup
        layoutManager = LinearLayoutManager(context)
        val recycler_view =view.findViewById<RecyclerView>(R.id.recyclerView_profile)
        adapter = RecyclerViewAdapter()
        recycler_view.adapter = adapter
        recycler_view.layoutManager = layoutManager

        //Fill own products

        userViewModel.getUserWithProducts(auth.currentUser!!.email!!).observe(viewLifecycleOwner,
            Observer {
                (adapter as RecyclerViewAdapter).setData(it[0].product)
            })


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
                    sharedViewModel.saveUserEmail(auth.currentUser!!.email!!.toString())
                    Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_editProfileFragment)
                    true
                }
                R.id.edit_email_or_password ->{
                    Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_editEmailPasswordFragment)
                    true
                }
                R.id.change_image ->{
                    uploadImage()
                    true
                }
                else -> false
                }
            }
        //inflate view
        return view
    }




    private fun setInformations(view:View){
        userViewModel.getUserByEmail(auth.currentUser!!.email!!).observe(viewLifecycleOwner,
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