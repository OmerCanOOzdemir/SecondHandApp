package com.example.secondhandapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.secondhandapp.data.user.User
import com.example.secondhandapp.data.user.UserDAO
import com.example.secondhandapplication.data.converter.Converters
import com.example.secondhandapplication.data.product.Product
import com.example.secondhandapplication.data.product.ProductDAO

@Database(entities = [User::class, Product::class], version =5, exportSchema = false)
@TypeConverters(Converters::class)
abstract class SecondHandDatabase: RoomDatabase() {

    abstract fun userDao(): UserDAO
    abstract fun productDao():ProductDAO

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: SecondHandDatabase? = null

        fun getDatabase(context: Context): SecondHandDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SecondHandDatabase::class.java,
                    "second_hand_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

    }