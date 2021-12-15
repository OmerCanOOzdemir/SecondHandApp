package com.example.secondhandapplication


import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.secondhandapp.data.database.SecondHandDatabase
import com.example.secondhandapp.data.user.User
import com.example.secondhandapp.data.user.UserDAO
import com.example.secondhandapplication.data.address.Address
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@RunWith(AndroidJUnit4::class)
class ExampleUnitTest {


    private lateinit var database : SecondHandDatabase
    private lateinit var userDAO: UserDAO

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context,SecondHandDatabase::class.java).build()
        userDAO = database.userDao()
    }

    @Test
    fun addUserAndReadTest()= runBlocking{
        val user = User("unit@test.com","junit","4","4444444",null, Address("unit street",4))
        userDAO.addUser(user)

        val dbUser = userDAO.getUserByEmailTesting("unit@test.com")

        assertEquals(user,dbUser)
    }

    @After
    fun closeDB(){
        database.close()
    }

}