package com.example.diplomv1.presentation.register

interface RegisterContract {
    interface View {
        fun showRegisterSuccess()
        fun showRegisterError(message: String)
    }

    interface Presenter {
        fun register(login: String, password: String)
    }
}