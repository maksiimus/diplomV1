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
    private var childId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_measurement_list)

        layout = findViewById(R.id.measurement_list_container)
        addButton = findViewById(R.id.button_add_measurement)

        childId = intent.getIntExtra("childId", -1)
        val childName = intent.getStringExtra("childName") ?: ""

        val resultsButton = findViewById<Button>(R.id.button_results)
        resultsButton.setOnClickListener {
            val intent = Intent(this, MeasurementResultsOverviewActivity::class.java)
            intent.putExtra("childId", childId)
            startActivity(intent)
        }

        val backButton = findViewById<Button>(R.id.buttonBackToChildren)
        backButton.setOnClickListener {
            finish() // вернёт в ChildListActivity
        }


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
    }

    override fun onResume() {
        super.onResume()
        if (childId != -1) {
            loadMeasurements(childId)
        }
    }

    private fun loadMeasurements(childId: Int) {
        layout.removeAllViews()
        val db = AppDatabase.getInstance(applicationContext)

        scope.launch {
            val measurements = withContext(Dispatchers.IO) {
                db.measurementDao().getAllByChild(childId)
            }

            val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val inflater = layoutInflater

            measurements.forEach { m: Measurement ->
                val card = inflater.inflate(R.layout.item_measurement_card, layout, false)

                val dateText = card.findViewById<TextView>(R.id.textMeasurementDate)
                val paramText = card.findViewById<TextView>(R.id.textMeasurementParams)
                val resultButton = card.findViewById<Button>(R.id.buttonMeasurementResults)

                val formattedDate = dateFormat.format(Date(m.date))
                dateText.text = "Дата: $formattedDate"

                val text = buildString {
                    if (m.height != null) append("📏 Рост: ${m.height} см\n")
                    if (m.weight != null) append("⚖️ Вес: ${m.weight} кг\n")
                    if (m.headCircumference != null) append("🧠 ОГ: ${m.headCircumference} см\n")
                    if (m.chestCircumference != null) append("❤️ ОГр: ${m.chestCircumference} см\n")
                    if (m.note.isNotBlank()) append("📝 ${m.note}")
                }
                paramText.text = text.trim()

                resultButton.setOnClickListener {
                    val intent = Intent(this@MeasurementListActivity, MeasurementResultsOverviewActivity::class.java)
                    intent.putExtra("childId", childId)
                    intent.putExtra("measurementId", m.id) // на будущее
                    startActivity(intent)
                }

                layout.addView(card)
            }
        }
    }

}
