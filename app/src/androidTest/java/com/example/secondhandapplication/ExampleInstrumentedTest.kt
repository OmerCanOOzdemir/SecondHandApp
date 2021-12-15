package com.example.secondhandapplication

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.secondhandapp.data.database.SecondHandDatabase
import com.example.secondhandapplication.data.category.Category
import com.example.secondhandapplication.data.category.CategoryDAO
import kotlinx.coroutines.runBlocking
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    private lateinit var database : SecondHandDatabase
    private lateinit var categoryDAO: CategoryDAO

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, SecondHandDatabase::class.java).build()
        categoryDAO = database.categoryDao()
    }

    @Test
    fun addCategoryAndReadTest()= runBlocking{

        val category = Category(999999999,"Voeding")
        categoryDAO.addCategory(category)
        val dbCategory = categoryDAO.getCategoryByName("Voeding")
        assertEquals(category,dbCategory)
        //Delete category after testing
        categoryDAO.deleteCategory(dbCategory.id)
    }

    @After
    fun closeDB(){
        database.close()
    }
}