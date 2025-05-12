package com.example.diplomv1.presentation.measurement


import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.diplomv1.R
import com.example.diplomv1.data.db.AppDatabase
import com.example.diplomv1.data.model.Measurement
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class AddMeasurementActivity : AppCompatActivity() {

    private val scope = CoroutineScope(Dispatchers.Main + Job())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_measurement)

        val childId = intent.getIntExtra("childId", -1)
        if (childId == -1) {
            Toast.makeText(this, "Ребёнок не найден", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val dateField = findViewById<EditText>(R.id.editMeasurementDate)
        val heightField = findViewById<EditText>(R.id.editHeight)
        val weightField = findViewById<EditText>(R.id.editWeight)
        val headField = findViewById<EditText>(R.id.editHead)
        val chestField = findViewById<EditText>(R.id.editChest)
        val noteField = findViewById<EditText>(R.id.editNote)
        val button = findViewById<Button>(R.id.buttonSaveMeasurement)

        val calendar = Calendar.getInstance()
        dateField.setText(SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(calendar.time))

        dateField.setOnClickListener {
            DatePickerDialog(
                this,
                { _, y, m, d ->
                    val formatted = "%02d.%02d.%04d".format(d, m + 1, y)
                    dateField.setText(formatted)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        button.setOnClickListener {
            val height = heightField.text.toString().toFloatOrNull()
            val weight = weightField.text.toString().toFloatOrNull()
            val head = headField.text.toString().toFloatOrNull()
            val chest = chestField.text.toString().toFloatOrNull()
            val note = noteField.text.toString()
            val date = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                .parse(dateField.text.toString())?.time ?: 0L

            if (height == null && weight == null && head == null) {
                Toast.makeText(this, "Введите хотя бы один параметр", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val measurement = Measurement(
                childId = childId,
                height = height,
                weight = weight,
                headCircumference = head,
                chestCircumference = chest, // 👈 сюда
                date = date,
                note = note
            )


            scope.launch {
                val db = AppDatabase.getInstance(applicationContext)
                withContext(Dispatchers.IO) {
                    db.measurementDao().insert(measurement)
                }
                Toast.makeText(this@AddMeasurementActivity, "Измерение добавлено", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}