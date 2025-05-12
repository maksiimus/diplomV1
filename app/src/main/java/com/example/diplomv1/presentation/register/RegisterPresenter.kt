package com.example.diplomv1.presentation.register

import com.example.diplomv1.data.db.AppDatabase
import com.example.diplomv1.data.model.User
import kotlinx.coroutines.*
import java.security.MessageDigest

class RegisterPresenter(
    private val view: RegisterContract.View,
    private val db: AppDatabase
) : RegisterContract.Presenter {

    private val scope = CoroutineScope(Dispatchers.Main + Job())

    override fun register(login: String, password: String) {
        scope.launch {
            val userDao = db.userDao()

            val existing = withContext(Dispatchers.IO) {
                userDao.getByLogin(login)
            }

            if (existing != null) {
                view.showRegisterError("Пользователь уже существует")
                return@launch
            }

            val newUser = User(
                login = login,
                passwordHash = hash(password),
                createdAt = System.currentTimeMillis()
            )

            withContext(Dispatchers.IO) {
                userDao.insert(newUser)
            }

            view.showRegisterSuccess()
        }
    }

    private fun hash(input: String): String {
        return MessageDigest.getInstance("SHA-256")
            .digest(input.toByteArray())
            .joinToString("") { "%02x".format(it) }
    }
}