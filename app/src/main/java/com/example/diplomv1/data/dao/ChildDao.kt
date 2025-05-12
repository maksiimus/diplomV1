package com.example.diplomv1.data.dao

import androidx.room.*
import com.example.diplomv1.data.model.Child

@Dao
interface ChildDao {
    @Insert suspend fun insert(child: Child): Long

    @Query("SELECT * FROM children WHERE userId = :userId ORDER BY surname ASC")
    suspend fun getAllByUser(userId: Int): List<Child>

    @Delete suspend fun delete(child: Child)
}
