package com.example.secondhandapplication.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.secondhandapp.data.user.UserViewModel
import com.example.secondhandapplication.R
import com.example.secondhandapplication.data.product.Product
import com.example.secondhandapplication.data.product.ProductViewModel
import com.example.secondhandapplication.databinding.FragmentAddProductBinding
import com.example.secondhandapplication.databinding.FragmentHomeBinding
import com.example.secondhandapplication.databinding.FragmentProfileBinding


class AddProductFragment : Fragment() {

    private lateinit var productViewModel: ProductViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        
        val view: View = inflater.inflate(R.layout.fragment_add_product,container,false)
        val add_product_btn = view.findViewById<Button>(R.id.add_product_btn)


        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)

        //Add product
        add_product_btn.setOnClickListener     {

        val title = view.findViewById<EditText>(R.id.title_product_input)
        val description = view.findViewById<EditText>(R.id.description_product_input)
        val price = view.findViewById<EditText>(R.id.price_product_input)

        val product = Product(0,title.text.toString(),description.text.toString(),price.text.toString().toDouble())
        productViewModel.addProduct(product)
        //Redirect to home page
        Navigation.findNavController(view).navigate(R.id.action_addProductFragment_to_homeFragment)
        Toast.makeText(activity,getString(R.string.product_created),Toast.LENGTH_LONG).show()
        }

        return view
    }





}