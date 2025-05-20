package com.example.diplomv1.lms

enum class Gender {
    BOY, GIRL;
    fun folderName(): String = if (this == BOY) "boys" else "girls"
}