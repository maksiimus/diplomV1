package com.example.diplomv1.presentation.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.diplomv1.MainActivity
import com.example.diplomv1.R
import com.example.diplomv1.data.db.AppDatabase
import com.example.diplomv1.presentation.register.RegisterActivity
import com.example.diplomv1.utils.EncryptedPrefs
import com.example.diplomv1.utils.LMSCalculator

import kotlinx.coroutines.*

class LoginActivity : AppCompatActivity(), LoginContract.View {

    private lateinit var presenter: LoginContract.Presenter
    private val scope = CoroutineScope(Dispatchers.Main + Job())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val storedUserId = EncryptedPrefs.getUserId(this)
        if (storedUserId != -1) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_login)
        val x = 15f
        val l = -1.22f
        val m = 14.79f
        val s = 0.077f

        val z = LMSCalculator.calculateZ(x, l, m, s)
        val centile = LMSCalculator.calculateCentile(z)

        Toast.makeText(this, "Z = %.2f\nCentile = %.1f".format(z, centile), Toast.LENGTH_LONG).show()


        val db = AppDatabase.getInstance(applicationContext)
        presenter = LoginPresenter(this, db)

        val loginField = findViewById<EditText>(R.id.editLogin)
        val passwordField = findViewById<EditText>(R.id.editPassword)
        val button = findViewById<Button>(R.id.buttonLogin)
        val registerButton = findViewById<Button>(R.id.buttonToRegister)

        button.setOnClickListener {
            val login = loginField.text.toString()
            val password = passwordField.text.toString()
            presenter.login(login, password)
        }

        registerButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    override fun showLoginSuccess() {
        Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT).show()

        val db = AppDatabase.getInstance(applicationContext)
        val loginText = findViewById<EditText>(R.id.editLogin).text.toString()

        scope.launch {
            val userDao = db.userDao()
            val user = withContext(Dispatchers.IO) {
                userDao.getByLogin(loginText)
            }

            if (user != null) {
                EncryptedPrefs.saveUserId(this@LoginActivity, user.id)
            }

            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
    }

    override fun showLoginError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
