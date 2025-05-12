package com.example.diplomv1.presentation.register

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.diplomv1.R
import com.example.diplomv1.data.db.AppDatabase

class RegisterActivity : AppCompatActivity(), RegisterContract.View {

    private lateinit var presenter: RegisterContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val db = AppDatabase.getInstance(applicationContext)
        presenter = RegisterPresenter(this, db)

        val loginField = findViewById<EditText>(R.id.editLogin)
        val passwordField = findViewById<EditText>(R.id.editPassword)
        val button = findViewById<Button>(R.id.buttonRegister)

        button.setOnClickListener {
            val login = loginField.text.toString()
            val password = passwordField.text.toString()
            presenter.register(login, password)
        }
    }

    override fun showRegisterSuccess() {
        Toast.makeText(this, "Регистрация успешна", Toast.LENGTH_SHORT).show()
        finish() // вернуться на экран входа
    }

    override fun showRegisterError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}