package com.example.secondhandapplication.data.product

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.secondhandapplication.data.relations.UserWithProducts


class ProductRepository(private val productDAO: ProductDAO) {



    fun getAllProducts(): LiveData<List<Product>> {
        return productDAO.getAllProducts()
    }


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun addProduct(product: Product){
        productDAO.addProduct(product)
    }

    fun getProductByTitle(title:String): LiveData<List<Product>> {
        return productDAO.getProductByTitle(title)
    }



}