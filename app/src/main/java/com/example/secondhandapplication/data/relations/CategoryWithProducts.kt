package com.example.secondhandapplication.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.secondhandapplication.data.category.Category
import com.example.secondhandapplication.data.product.Product

data class CategoryWithProducts (
    @Embedded
    val category: Category,
    @Relation(
        parentColumn = "id",
        entityColumn = "category_id"
    )
    val product: List<Product>
        )