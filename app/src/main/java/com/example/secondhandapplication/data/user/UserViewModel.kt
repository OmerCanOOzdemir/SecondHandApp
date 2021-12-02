package com.example.secondhandapp.data.user

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.secondhandapp.data.database.SecondHandDatabase
import kotlinx.coroutines.launch

class UserViewModel(application: Application):AndroidViewModel(application) {

    private val repository: UserRepository
    init {
        val userdao = SecondHandDatabase.getDatabase(application).userDao()
        repository = UserRepository(userdao)
    }

    fun getAuthUser(email:String):LiveData<User>{
        return repository.getAuthUser(email)
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
}