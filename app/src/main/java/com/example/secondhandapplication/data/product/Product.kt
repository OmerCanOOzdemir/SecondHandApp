package com.example.secondhandapplication.data.product

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class Product (  @PrimaryKey(autoGenerate = true)
                      @ColumnInfo(name = "id")
                      val id : Int,
                      @ColumnInfo(name = "title")
                      val title :String,
                      @ColumnInfo(name = "description")
                      val description : String,
                      @ColumnInfo(name = "price")
                      val price: Double
                      )