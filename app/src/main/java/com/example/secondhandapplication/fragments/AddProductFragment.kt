package com.example.secondhandapplication.fragments

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.secondhandapplication.R
import com.example.secondhandapplication.data.category.CategoryViewModel
import com.example.secondhandapplication.data.product.Product
import com.example.secondhandapplication.data.product.ProductViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.*


class AddProductFragment : Fragment() {

    private lateinit var productViewModel: ProductViewModel
    private lateinit var categoryViewModel: CategoryViewModel
    private var image: Bitmap? = null
    private lateinit var auth:FirebaseAuth
    private lateinit var autoCompleteTextView:AutoCompleteTextView

    // https://developer.android.com/training/basics/intents/result
    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        val resolver = requireActivity().contentResolver
        //getBitmap is deprecated
        if(Build.VERSION.SDK_INT <28){
            val bitmap = MediaStore.Images.Media.getBitmap(resolver,uri)
            if(bitmap != null){
                image = bitmap
            }

        }else{
            val source = uri?.let { ImageDecoder.createSource(resolver, it) }
            if(source != null){
                image = ImageDecoder.decodeBitmap(source)
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_add_product,container,false)

        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        categoryViewModel = ViewModelProvider(this).get(CategoryViewModel::class.java)

        autoCompleteTextView = view.findViewById(R.id.choose_category)
        //Get all categories
        categoryViewModel.allCategories.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            val adapter = ArrayAdapter(requireContext(),R.layout.dropdown_category,it)
            autoCompleteTextView.setAdapter(adapter)
        })
        val add_product_btn = view.findViewById<Button>(R.id.add_product_btn)
        val upload_image_btn = view.findViewById<Button>(R.id.upload_product_image_btn)

        //Get auth info
        auth = FirebaseAuth.getInstance()



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
        val upload_image_btn = view.findViewById<Button>(R.id.upload_product_image_btn)
        if(TextUtils.isEmpty(title.text)){
            title.error = getString(R.string.title_product_empty)
            title.requestFocus()
        }else if(TextUtils.isEmpty(description.text)){
            description.error = getString(R.string.description_product_empty)
            description.requestFocus()
        }else if(TextUtils.isEmpty(price.text)){
            price.error = getString(R.string.price_empty_product)
            price.requestFocus()
        }else if(autoCompleteTextView.text.toString() == getString(R.string.choose_category)){
            autoCompleteTextView.error = getString(R.string.choose_category)
            autoCompleteTextView.requestFocus()
        }else if (image == null){
            upload_image_btn.error = getString(R.string.choose_image_error)
            upload_image_btn.requestFocus()
        }

        else{
                categoryViewModel.getIdOfCategory(autoCompleteTextView.text.toString()).observe(viewLifecycleOwner,
                androidx.lifecycle.Observer {
                    val date = Date()
                    val product = Product(0,title.text.toString(),description.text.toString(),price.text.toString().toDouble(),
                        image!!,date,auth.currentUser!!.email!!,
                        it
                    )
                    productViewModel.addProduct(product)
                    //Redirect to home page
                    Navigation.findNavController(view).navigate(R.id.action_addProductFragment_to_homeFragment)
                    Toast.makeText(activity,getString(R.string.product_created),Toast.LENGTH_LONG).show()
                })

        }


    }



    private fun uploadImage() {
        getContent.launch("image/*")
    }


}