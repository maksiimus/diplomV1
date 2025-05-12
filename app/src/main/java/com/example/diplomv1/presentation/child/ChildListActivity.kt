package com.example.diplomv1.presentation.child

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.diplomv1.R
import com.example.diplomv1.data.db.AppDatabase
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

        layout.removeAllViews() // Очистка старого списка перед загрузкой

        val db = AppDatabase.getInstance(applicationContext)
        scope.launch {
            val children = withContext(Dispatchers.IO) {
                db.childDao().getAll()
            }

            children.forEach { child ->
                val tv = TextView(this@ChildListActivity)
                tv.text = "${child.surname} ${child.name} ${child.patronymic}"
                tv.textSize = 18f
                layout.addView(tv)
            }
        }
    }
}
