package com.example.secondhandapp.data.user

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.secondhandapp.data.database.SecondHandDatabase
import com.example.secondhandapplication.data.relations.UserWithProducts
import kotlinx.coroutines.launch

class UserViewModel(application: Application):AndroidViewModel(application) {

    private val repository: UserRepository
    init {
        val userdao = SecondHandDatabase.getDatabase(application).userDao()
        repository = UserRepository(userdao)
    }

    fun getUserByEmail(email:String):LiveData<User>{
        return repository.getUserByEmail(email)
    }
    fun addUser(user:User){
        viewModelScope.launch {
            repository.addUser(user)
        }
    }
    fun updateUser(user:User){
        viewModelScope.launch {
            repository.updateUser(user)
        }
    }
    fun updateImageUser(email: String,image: Bitmap){
        viewModelScope.launch {
            repository.updateImageUser(email,image)
        }
    }
    fun updateEmailUser(old_email: String,new_email: String){
        viewModelScope.launch {
            repository.updateEmailUser(old_email,new_email)
        }
    }
    fun deleteByEmailUser(email: String){
        viewModelScope.launch {
            repository.deleteEmailByUser(email)
        }
    }
    fun getUserWithProducts(email: String): LiveData<List<UserWithProducts>> {
        return repository.getUserWithProducts(email)
    }

    fun getUserByEmailTesting(email:String): User{
        return repository.getUserByEmailTesting(email)
    }
}