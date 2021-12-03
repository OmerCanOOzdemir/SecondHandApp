package com.example.secondhandapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.secondhandapp.entities.RecyclerViewAdapter
import com.example.secondhandapplication.R
import com.example.secondhandapplication.data.category.CategoryViewModel
import com.example.secondhandapplication.data.product.Product
import com.example.secondhandapplication.data.product.ProductViewModel
import com.example.secondhandapplication.databinding.FragmentHomeBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton


class HomeFragment : Fragment() {

    private lateinit var productViewModel: ProductViewModel
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var adapter: RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
    private lateinit var binding: FragmentHomeBinding
    private lateinit var autoCompleteTextView:AutoCompleteTextView
    private lateinit var categoryViewModel: CategoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)
        val view = binding.root


        //Recyclerview setup
        layoutManager = LinearLayoutManager(context)
        val recycler_view =view.findViewById<RecyclerView>(R.id.recyclerView)
        adapter = RecyclerViewAdapter()
        recycler_view.adapter = adapter
        recycler_view.layoutManager = layoutManager
        val add_product_btn = view.findViewById<FloatingActionButton>(R.id.add_product)
        //productViewModel
        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        productViewModel.allProducts.observe(viewLifecycleOwner, Observer {
            (adapter as RecyclerViewAdapter).setData(it)
        })

        //Setup search view
        val search = binding.searchProduct
        search.isSubmitButtonEnabled = true
        search.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                search.clearFocus()
                if (query != null) {
                    search(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    search(newText)
                }
                return true
            }

        })

        //Setup category filter
        categoryViewModel = ViewModelProvider(this).get(CategoryViewModel::class.java)
        autoCompleteTextView = view.findViewById(R.id.filter_category)
        //Get all categories
        categoryViewModel.allCategories.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            val adapter = ArrayAdapter(requireContext(),R.layout.dropdown_category,it)
            autoCompleteTextView.setAdapter(adapter)
        })
        //Filter product
        autoCompleteTextView.setOnItemClickListener { adapterView, view, position, id ->
            val category = adapterView.getItemAtPosition(position).toString()
            filterByCategory(category)
        }

        // Navigate to add product fragment
        add_product_btn.setOnClickListener{
            Navigation.findNavController(it).navigate(R.id.to_add_product_fragment)
        }
        return view
    }

    private fun search(query:String){
        val searchQuery = "%$query%"
        productViewModel.getProductByTitle(searchQuery).observe(viewLifecycleOwner,{
            (adapter as RecyclerViewAdapter).setData(it)
        })
    }
    private fun filterByCategory(category:String){
        categoryViewModel.getProductsByCategory(category).observe(viewLifecycleOwner,{
            (adapter as RecyclerViewAdapter).setData(it[0].product)
        })
    }

}