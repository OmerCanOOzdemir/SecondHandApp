package com.example.secondhandapplication.fragments

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.secondhandapplication.R
import com.example.secondhandapplication.data.product.Product
import com.example.secondhandapplication.data.product.ProductViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.*


class AddProductFragment : Fragment() {

    private lateinit var productViewModel: ProductViewModel
    private lateinit var image: Bitmap
    private lateinit var auth:FirebaseAuth

    // https://developer.android.com/training/basics/intents/result
    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        val resolver = requireActivity().contentResolver
        //getBitmap is deprecated
        if(Build.VERSION.SDK_INT <28){
            val bitmap = MediaStore.Images.Media.getBitmap(resolver,uri!!)
            image = bitmap
        }else{
            val source = ImageDecoder.createSource(resolver, uri!!)
            image = ImageDecoder.decodeBitmap(source)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        
        val view: View = inflater.inflate(R.layout.fragment_add_product,container,false)
        val add_product_btn = view.findViewById<Button>(R.id.add_product_btn)
        val upload_image_btn = view.findViewById<Button>(R.id.upload_product_image_btn)

        //Get auth info
        auth = FirebaseAuth.getInstance()

        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)

        //Add product
        add_product_btn.setOnClickListener     {
            storeProduct(view)
        }
        //Upload image
        upload_image_btn.setOnClickListener {
            uploadImage()
        }


        return view
    }
    private fun storeProduct(view: View){
        val title = view.findViewById<EditText>(R.id.title_product_input)
        val description = view.findViewById<EditText>(R.id.description_product_input)
        val price = view.findViewById<EditText>(R.id.price_product_input)

        if(TextUtils.isEmpty(title.text)){
            title.error = getString(R.string.title_product_empty)
            title.requestFocus()
        }else if(TextUtils.isEmpty(description.text)){
            description.error = getString(R.string.description_product_empty)
            description.requestFocus()
        }else if(TextUtils.isEmpty(price.text)){
            price.error = getString(R.string.price_empty_product)
            price.requestFocus()
        }

        val date = Date()
        val product = Product(0,title.text.toString(),description.text.toString(),price.text.toString().toDouble(),image,date,
            auth.currentUser!!.email!!
        )
        productViewModel.addProduct(product)
        //Redirect to home page
        Navigation.findNavController(view).navigate(R.id.action_addProductFragment_to_homeFragment)
        Toast.makeText(activity,getString(R.string.product_created),Toast.LENGTH_LONG).show()
    }



    private fun uploadImage() {
        getContent.launch("image/*")
    }


}