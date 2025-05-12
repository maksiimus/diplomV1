package com.example.diplomv1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.diplomv1.utils.EncryptedPrefs

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val logoutButton = findViewById<Button>(R.id.buttonLogout)
        logoutButton.setOnClickListener {
            val prefs = EncryptedPrefs.getPrefs(this)
            prefs.edit().remove("login").apply()

            startActivity(Intent(this, com.example.diplomv1.presentation.login.LoginActivity::class.java))
            finish()
        }
    }
}
