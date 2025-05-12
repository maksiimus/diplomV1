package com.example.diplomv1.data.dao


import androidx.room.*
import com.example.diplomv1.data.model.User

@Dao
interface UserDao {
    @Insert suspend fun insert(user: User): Long
    @Query("SELECT * FROM users WHERE login = :login") suspend fun getByLogin(login: String): User?
}