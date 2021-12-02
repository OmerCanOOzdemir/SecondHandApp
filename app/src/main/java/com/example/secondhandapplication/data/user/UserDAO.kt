package com.example.secondhandapp.data.user

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDAO {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    @Update
    suspend fun updateUser(user:User)

    @Query("Update user set profile_image = :image Where email = :email")
    suspend fun updateImageUser(image:Bitmap,email: String)

    @Query("Select * from user where email=:email")
    fun getAuthUserInfo(email:String): LiveData<User>

    @Query("Update user set email = :new_email where email = :old_email")
    suspend fun updateEmailUser(old_email:String,new_email:String)



}