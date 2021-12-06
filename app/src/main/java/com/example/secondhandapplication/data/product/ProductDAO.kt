package com.example.secondhandapplication.data.product

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.secondhandapp.data.user.User
import com.example.secondhandapplication.data.relations.UserWithProducts


@Dao
interface ProductDAO {


    @Insert
    suspend fun addProduct(product: Product)

    @Query("Select * from product")
    fun getAllProducts(): LiveData<List<Product>>

    @Query("Select * from product where title like :title")
    fun getProductByTitle(title:String):LiveData<List<Product>>

    @Query("Select * from product where id = :id")
    fun getProductById(id:Int):LiveData<Product>

    @Delete
    suspend fun deleteProduct(product: Product)

    @Update
    suspend fun updateProduct(product: Product)

    @Query("Update product set user_id = :new_email Where user_id = :old_mail")
    suspend fun updateProductUserId(old_mail: String, new_email: String)

    @Query("Delete from product  Where user_id = :email")
    suspend fun deleteAllUserProduct(email: String)



}