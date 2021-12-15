package com.example.secondhandapplication.data.category

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.secondhandapplication.data.product.Product
import com.example.secondhandapplication.data.relations.CategoryWithProducts


@Dao
interface CategoryDAO {
    @Insert
    suspend fun addCategory(category: Category)

    @Query("Select name from category")
    fun getAllCategories(): LiveData<List<String>>

    @Transaction
    @Query("Select * from category where name = :name")
    fun getProductsByCategory(name:String): LiveData<List<CategoryWithProducts>>

    @Query("Select id from category where name = :name")
    fun getIdOfCategory(name: String):LiveData<Int>

    @Query("Select * from category where name = :name")
    fun getCategoryByName(name : String):Category

    @Query("Delete from category  Where id = :id")
    suspend fun deleteCategory(id: Int)

}