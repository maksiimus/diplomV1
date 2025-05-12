package com.example.diplomv1.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "measurements")
data class Measurement(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val childId: Int,
    val height: Float?,
    val weight: Float?,
    val headCircumference: Float?,
    val chestCircumference: Float?, // 👈 новое поле
    val date: Long,
    val note: String
)
