package com.example.secondhandapp.data.user

import android.graphics.Bitmap
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.secondhandapplication.data.relations.UserWithProducts


class UserRepository(private val userDAO: UserDAO) {


    fun getUserByEmail(email:String): LiveData<User> {
        return userDAO.getUserByEmail(email)
    }


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun addUser(user: User){
        userDAO.addUser(user)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateUser(user: User){
        userDAO.updateUser(user)
    }
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateImageUser(email: String,image:Bitmap){
        userDAO.updateImageUser(image,email)
    }
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateEmailUser(old_email: String,new_email:String){
        userDAO.updateEmailUser(old_email,new_email)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteEmailByUser(email: String){
        userDAO.deleteUserByEmail(email)
    }


    fun getUserWithProducts(email: String): LiveData<List<UserWithProducts>> {
        return userDAO.getUserWithProducts(email)
    }
}