package com.example.secondhandapp.data.user

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.secondhandapplication.data.relations.UserWithProducts

@Dao
interface UserDAO {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    @Update
    suspend fun updateUser(user:User)

    @Query("Update user set profile_image = :image Where email = :email")
    suspend fun updateImageUser(image:Bitmap,email: String)

    @Query("Select * from user where email=:email")
    fun getUserByEmail(email:String): LiveData<User>

    @Query("Update user set email = :new_email where email = :old_email")
    suspend fun updateEmailUser(old_email:String,new_email:String)

    @Query("Delete from user where email = :email")
    suspend fun deleteUserByEmail(email:String)

    @Transaction
    @Query("SELECT * FROM User where email = :email")
    fun getUserWithProducts(email:String): LiveData<List<UserWithProducts>>



    @Query("Select * from user where email=:email")
    fun getUserByEmailTesting(email:String): User



}