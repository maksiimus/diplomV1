package com.example.diplomv1.data.dao

import androidx.room.*
import com.example.diplomv1.data.model.Measurement

@Dao
interface MeasurementDao {

    @Insert suspend fun insert(measurement: Measurement): Long

    @Query("SELECT * FROM measurements WHERE childId = :childId ORDER BY date DESC")
    suspend fun getAllByChild(childId: Int): List<Measurement>

    @Delete suspend fun delete(measurement: Measurement)
}