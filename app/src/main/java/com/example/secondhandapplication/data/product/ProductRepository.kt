package com.example.secondhandapplication.data.product

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData


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