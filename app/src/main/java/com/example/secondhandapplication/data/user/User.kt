package com.example.secondhandapp.data.user

import android.graphics.Bitmap
import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.secondhandapplication.data.address.Address


@Entity(tableName = "user")
data class User (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "email")
    val email:String,
    @ColumnInfo(name = "firstname")
    val firstname:String,
    @ColumnInfo(name = "lastname")
    val lastname:String,

    @ColumnInfo(name = "phone")
    val phone:String,
    @ColumnInfo(name = "profile_image")
    @Nullable
    var image:Bitmap,
    @Embedded
    var address: Address

        )
