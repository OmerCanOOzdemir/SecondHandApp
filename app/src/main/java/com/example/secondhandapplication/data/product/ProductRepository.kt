package com.example.secondhandapplication.data.product

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.secondhandapp.data.user.User
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

    fun getProductById(id:Int):LiveData<Product>{
        return productDAO.getProductById(id)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteProduct(product: Product){
        return productDAO.deleteProduct(product)
    }
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateUser(product: Product){
        productDAO.updateProduct(product)
    }
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateProductUserId(old_mail: String, new_email: String){
        productDAO.updateProductUserId(old_mail,new_email)
    }
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAllUserProduct(email: String){

        productDAO.deleteAllUserProduct(email)
        }


}