package com.example.secondhandapplication.data.product

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.secondhandapplication.data.relations.UserWithProducts


@Dao
interface ProductDAO {


    @Insert
    suspend fun addProduct(product: Product)

    @Query("Select * from product")
    fun getAllProducts(): LiveData<List<Product>>

    @Query("Select * from product where title like :title")
    fun getProductByTitle(title:String):LiveData<List<Product>>


}