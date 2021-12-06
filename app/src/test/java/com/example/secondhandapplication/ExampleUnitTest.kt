package com.example.secondhandapplication


import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.secondhandapp.data.database.SecondHandDatabase
import com.example.secondhandapp.data.user.User
import com.example.secondhandapp.data.user.UserDAO
import com.example.secondhandapplication.data.address.Address
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
/*
    private lateinit var database : SecondHandDatabase
    private lateinit var userDAO: UserDAO

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context,SecondHandDatabase::class.java).build()
        userDAO = database.userDao()
    }

    @Test
    fun addUserAndReadTest()= runBlocking{
        val user = User("unit@test.com","junit","4","4444444",null, Address("unit street",4))
        userDAO.addUser(user)

        val dbUser = userDAO.getAuthUserInfo("unit@test.com")

        assertEquals(user,dbUser)
    }

    @After
    fun closeDB(){
        database.close()
    }
    */

}