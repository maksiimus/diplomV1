package com.example.diplomv1.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "children")
data class Child(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val surname: String,
    val name: String,
    val patronymic: String,
    val birthDate: Long, // в миллисекундах
    val gender: String   // "Мальчик" или "Девочка"
)