package com.example.secondhandapplication.data.category

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class Category (
    @ColumnInfo(name="id")
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    @ColumnInfo(name = "name")
    val name:String

        )