package com.example.diplomv1.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.diplomv1.data.dao.UserDao
import com.example.diplomv1.data.dao.ChildDao
import com.example.diplomv1.data.dao.MeasurementDao
import com.example.diplomv1.data.model.User
import com.example.diplomv1.data.model.Child
import com.example.diplomv1.data.model.Measurement

@Database(entities = [User::class, Child::class, Measurement::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun childDao(): ChildDao
    abstract fun measurementDao(): MeasurementDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "user_database"
                )
                    .fallbackToDestructiveMigration()
                    .build().also { INSTANCE = it }
            }
        }
    }
}
