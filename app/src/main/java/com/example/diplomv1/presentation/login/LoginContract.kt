package com.example.diplomv1.presentation.login

interface LoginContract {
    interface View {
        fun showLoginSuccess()
        fun showLoginError(message: String)
    }

    interface Presenter {
        fun login(login: String, password: String)
    }
}