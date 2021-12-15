package com.example.secondhandapplication.shared

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel:ViewModel() {
    private var _userId=MutableLiveData("user")
    val userEmail :LiveData<String> = _userId
    fun saveUserEmail(email: String){
        _userId.value = email
    }
}