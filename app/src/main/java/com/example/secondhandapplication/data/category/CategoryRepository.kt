package com.example.secondhandapplication.data.category

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.secondhandapplication.data.product.Product
import com.example.secondhandapplication.data.product.ProductDAO
import com.example.secondhandapplication.data.relations.CategoryWithProducts

class CategoryRepository(private val categoryDAO: CategoryDAO) {



    fun getAllCategories(): LiveData<List<String>> {
        return categoryDAO.getAllCategories()
    }


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun addCategory(category: Category){
        categoryDAO.addCategory(category)
    }

    fun getProductsByCategory(name:String): LiveData<List<CategoryWithProducts>>{
        return categoryDAO.getProductsByCategory(name)
    }
    fun getIdOfCategory(name: String):LiveData<Int>{
        return categoryDAO.getIdOfCategory(name)
    }




}