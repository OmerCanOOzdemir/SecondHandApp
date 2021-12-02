package com.example.secondhandapplication.data.product

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface ProductDAO {


    @Insert
    suspend fun addProduct(product: Product)

    @Query("Select * from product")
    fun getAllProducts(): LiveData<List<Product>>

    @Query("Select * from product where title like :title")
    fun getProductByTitle(title:String):LiveData<List<Product>>
}