package com.example.diplomv1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.diplomv1.lms.*
import com.example.diplomv1.presentation.child.ChildListActivity
import com.example.diplomv1.presentation.login.LoginActivity
import com.example.diplomv1.utils.EncryptedPrefs

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        LMSRepository.init(applicationContext)

        val logoutButton = findViewById<Button>(R.id.buttonLogout)
        logoutButton.setOnClickListener {
            EncryptedPrefs.clearUserId(this)
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        val toChildrenButton = findViewById<Button>(R.id.buttonToChildren)
        toChildrenButton.setOnClickListener {
            startActivity(Intent(this, ChildListActivity::class.java))
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        LMSRepository.clear()
    }
}
