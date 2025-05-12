package com.example.diplomv1.presentation.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.diplomv1.MainActivity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.diplomv1.R
import com.example.diplomv1.data.db.AppDatabase
import com.example.diplomv1.presentation.register.RegisterActivity

class LoginActivity : AppCompatActivity(), LoginContract.View {

    private lateinit var presenter: LoginContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = com.example.diplomv1.utils.EncryptedPrefs.getPrefs(this)
        if (prefs.getString("login", "") == "1") {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }
        setContentView(R.layout.activity_login)

        val db = AppDatabase.getInstance(applicationContext)
        presenter = LoginPresenter(this, db)

        val loginField = findViewById<EditText>(R.id.editLogin)
        val passwordField = findViewById<EditText>(R.id.editPassword)
        val button = findViewById<Button>(R.id.buttonLogin)
        val registerButton = findViewById<Button>(R.id.buttonToRegister) // 👈 добавлено

        button.setOnClickListener {
            val login = loginField.text.toString()
            val password = passwordField.text.toString()
            presenter.login(login, password)
        }

        registerButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java)) // 👈 переход к регистрации
        }
    }

    override fun showLoginSuccess() {
        Toast.makeText(this, "Успешный вход", Toast.LENGTH_SHORT).show()
        val prefs = com.example.diplomv1.utils.EncryptedPrefs.getPrefs(this)
        prefs.edit().putString("login", "1").apply()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun showLoginError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}
