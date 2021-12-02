package com.example.secondhandapplication.data.product

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.secondhandapp.data.database.SecondHandDatabase
import com.example.secondhandapp.data.user.User
import com.example.secondhandapp.data.user.UserRepository
import com.example.secondhandapplication.data.relations.UserWithProducts
import kotlinx.coroutines.launch

class ProductViewModel(application: Application): AndroidViewModel(application) {
    private val repository: ProductRepository
    val allProducts :LiveData<List<Product>>
    init {
        val productDAO = SecondHandDatabase.getDatabase(application).productDao()
        repository = ProductRepository(productDAO)
        allProducts = repository.getAllProducts()
    }
    fun addProduct(product: Product){
        viewModelScope.launch {
            repository.addProduct(product)
        }
    }
    fun getProductByTitle(title:String): LiveData<List<Product>> {
        return repository.getProductByTitle(title)
    }

}