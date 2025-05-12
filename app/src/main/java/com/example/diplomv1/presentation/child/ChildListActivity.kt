package com.example.diplomv1.presentation.child

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.diplomv1.R
import com.example.diplomv1.data.db.AppDatabase
import com.example.diplomv1.data.model.Child
import com.example.diplomv1.utils.EncryptedPrefs
import kotlinx.coroutines.*

class ChildListActivity : AppCompatActivity() {

    private lateinit var layout: LinearLayout
    private lateinit var addButton: Button
    private val scope = CoroutineScope(Dispatchers.Main + Job())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_child_list)

        layout = findViewById(R.id.child_list_container)
        addButton = findViewById(R.id.button_add_child)

        addButton.setOnClickListener {
            startActivity(Intent(this, AddChildActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        layout.removeAllViews()

        val userId = EncryptedPrefs.getUserId(this)
        if (userId == -1) return

        val db = AppDatabase.getInstance(applicationContext)
        scope.launch {
            val children = withContext(Dispatchers.IO) {
                db.childDao().getAllByUser(userId)
            }
            val backButton = findViewById<Button>(R.id.buttonBackToMain)
            backButton.setOnClickListener {
                finish() // вернёт в MainActivity
            }


            children.forEach { child: Child ->
                val tv = TextView(this@ChildListActivity)
                tv.text = "${child.surname} ${child.name} ${child.patronymic}"
                tv.textSize = 18f
                tv.setPadding(0, 12, 0, 12)

                tv.setOnClickListener {
                    val intent = Intent(this@ChildListActivity, com.example.diplomv1.presentation.measurement.MeasurementListActivity::class.java)
                    intent.putExtra("childId", child.id)
                    intent.putExtra("childName", "${child.surname} ${child.name}")
                    startActivity(intent)
                }

                layout.addView(tv)
            }
        }
    }
}
