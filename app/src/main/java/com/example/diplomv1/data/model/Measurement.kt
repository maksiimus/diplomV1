package com.example.diplomv1.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "measurements")
data class Measurement(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val childId: Int,              // ID ребёнка
    val height: Float?,            // Рост, см
    val weight: Float?,            // Вес, кг
    val headCircumference: Float?, // Окружность головы, см
    val date: Long,                // Дата измерения (в мс)
    val note: String               // Комментарий
)