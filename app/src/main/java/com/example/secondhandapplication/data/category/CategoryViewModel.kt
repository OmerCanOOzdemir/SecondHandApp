package com.example.secondhandapplication.data.category

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.secondhandapp.data.database.SecondHandDatabase
import com.example.secondhandapplication.data.product.Product
import com.example.secondhandapplication.data.product.ProductRepository
import com.example.secondhandapplication.data.relations.CategoryWithProducts
import kotlinx.coroutines.launch

class CategoryViewModel(application: Application): AndroidViewModel(application) {
    private val repository: CategoryRepository
    val allCategories : LiveData<List<String>>

    init {
        val categoryDAO = SecondHandDatabase.getDatabase(application).categoryDao()
        repository = CategoryRepository(categoryDAO)
        allCategories = repository.getAllCategories()
    }



    fun addCategory(category: Category){
        viewModelScope.launch {
            repository.addCategory(category)
        }
    }
    fun getProductsByCategory(name:String): LiveData<List<CategoryWithProducts>>{
        return repository.getProductsByCategory(name)
    }
    fun getIdOfCategory(name: String):LiveData<Int>{
        return repository.getIdOfCategory(name)
    }
    fun getCategoryByName(name : String): Category {
        return repository.getCategoryByName(name);
    }
    suspend fun deleteCategory(id: Int){
        viewModelScope.launch {
            repository.deleteCategory(id);
        }
    }

}