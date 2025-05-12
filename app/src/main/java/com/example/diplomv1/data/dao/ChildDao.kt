package com.example.diplomv1.data.dao

import androidx.room.*
import com.example.diplomv1.data.model.Child

@Dao
interface ChildDao {
    @Insert suspend fun insert(child: Child): Long
    @Query("SELECT * FROM children ORDER BY surname ASC")
    suspend fun getAll(): List<Child>
    @Delete suspend fun delete(child: Child)
}