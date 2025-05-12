package com.example.diplomv1.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.diplomv1.data.dao.UserDao
import com.example.diplomv1.data.dao.ChildDao
import com.example.diplomv1.data.model.User
import com.example.diplomv1.data.model.Child

@Database(entities = [User::class, Child::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun childDao(): ChildDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "user_database"
                )
                    .fallbackToDestructiveMigration() // ⚠️ разрешаем пересоздание БД при изменениях схемы
                    .build().also { INSTANCE = it }
            }
        }
    }
}
