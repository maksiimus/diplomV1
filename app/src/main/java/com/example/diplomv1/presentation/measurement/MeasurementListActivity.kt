package com.example.diplomv1.presentation.measurement

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.diplomv1.R
import com.example.diplomv1.data.db.AppDatabase
import com.example.diplomv1.data.model.Measurement
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class MeasurementListActivity : AppCompatActivity() {

    private val scope = CoroutineScope(Dispatchers.Main + Job())
    private lateinit var layout: LinearLayout
    private lateinit var addButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_measurement_list)

        layout = findViewById(R.id.measurement_list_container)
        addButton = findViewById(R.id.button_add_measurement)

        val childId = intent.getIntExtra("childId", -1)
        val childName = intent.getStringExtra("childName") ?: ""

        if (childId == -1) {
            Toast.makeText(this, "Ребёнок не найден", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        title = "Измерения: $childName"

        addButton.setOnClickListener {
            val intent = Intent(this, AddMeasurementActivity::class.java)
            intent.putExtra("childId", childId)
            startActivity(intent)
        }

        loadMeasurements(childId)
    }

    private fun loadMeasurements(childId: Int) {
        layout.removeAllViews()
        val db = AppDatabase.getInstance(applicationContext)

        scope.launch {
            val measurements = withContext(Dispatchers.IO) {
                db.measurementDao().getAllByChild(childId)
            }

            val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

            measurements.forEach { m: Measurement ->
                val tv = TextView(this@MeasurementListActivity)
                val text = buildString {
                    append("Дата: ${dateFormat.format(Date(m.date))}\n")
                    if (m.height != null) append("Рост: ${m.height} см\n")
                    if (m.weight != null) append("Вес: ${m.weight} кг\n")
                    if (m.headCircumference != null) append("Окружность головы: ${m.headCircumference} см\n")
                    if (m.note.isNotBlank()) append("Комментарий: ${m.note}")
                }
                tv.text = text
                tv.setPadding(0, 12, 0, 12)
                layout.addView(tv)
            }
        }
    }
}