package com.example.secondhandapplication.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.secondhandapp.data.user.User
import com.example.secondhandapplication.data.product.Product

data class UserWithProducts (
    @Embedded val user: User,
    @Relation(
        parentColumn ="email",
        entityColumn = "user_id"
    )
    val product: List<Product>
        )