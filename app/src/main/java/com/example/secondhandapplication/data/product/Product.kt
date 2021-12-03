package com.example.secondhandapplication.data.product

import android.graphics.Bitmap
import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "product")
data class Product (  @PrimaryKey(autoGenerate = true)
                      @ColumnInfo(name = "id")
                      val id : Int,
                      @ColumnInfo(name = "title")
                      val title :String,
                      @ColumnInfo(name = "description")
                      val description : String,
                      @ColumnInfo(name = "price")
                      val price: Double,
                      @Nullable
                      @ColumnInfo(name="image")
                      val image:Bitmap,
                      @ColumnInfo(name="published_date")
                      val date: Date,
                      @ColumnInfo(name = "user_id")
                      val user_id:String,
                      @ColumnInfo(name = "category_id")
                      val category_id: Int
                      )