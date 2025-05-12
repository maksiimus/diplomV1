package com.example.diplomv1.presentation.login


import com.example.diplomv1.data.db.AppDatabase
import kotlinx.coroutines.*
import java.security.MessageDigest

class LoginPresenter(
    private val view: LoginContract.View,
    private val db: AppDatabase
) : LoginContract.Presenter {

    private val scope = CoroutineScope(Dispatchers.Main + Job())

    override fun login(login: String, password: String) {
        scope.launch {
            val userDao = db.userDao()
            val user = withContext(Dispatchers.IO) {
                userDao.getByLogin(login)
            }

            val passwordHash = hash(password)

            if (user == null || user.passwordHash != passwordHash) {
                view.showLoginError("Неверный логин или пароль")
            } else {
                view.showLoginSuccess()
            }
        }
    }

    private fun hash(input: String): String {
        return MessageDigest.getInstance("SHA-256")
            .digest(input.toByteArray())
            .joinToString("") { "%02x".format(it) }
    }
}