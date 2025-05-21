package com.example.diplomv1.presentation.measurement

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.diplomv1.R
import com.example.diplomv1.data.db.AppDatabase
import com.example.diplomv1.lms.CentileEvaluator
import com.example.diplomv1.lms.Gender
import com.example.diplomv1.lms.MeasurementType
import com.example.diplomv1.utils.EncryptedPrefs
import kotlinx.coroutines.*
import java.util.*
import kotlin.math.floor
import android.widget.Button

class MeasurementResultDetailActivity : AppCompatActivity() {

    private val scope = CoroutineScope(Dispatchers.Main + Job())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_measurement_result_detail)

        val childId = intent.getIntExtra("childId", -1)
        val parameter = intent.getStringExtra("parameter") ?: ""
        val measurementId = intent.getIntExtra("measurementId", -1) // добавлено

        val valueView = findViewById<TextView>(R.id.textValue)
        val centileView = findViewById<TextView>(R.id.textCentile)

        if (childId == -1 || parameter.isEmpty()) {
            valueView.text = "Ошибка: нет данных"
            return
        }

        val db = AppDatabase.getInstance(applicationContext)
        scope.launch {
            val measurements = withContext(Dispatchers.IO) {
                db.measurementDao().getAllByChild(childId)
            }

            val target = if (measurementId != -1) {
                measurements.find { it.id == measurementId }
            } else {
                measurements.firstOrNull()
            }

            if (target == null) {
                valueView.text = "Нет измерений"
                return@launch
            }

            val value = when (parameter) {
                "height" -> target.height
                "weight" -> target.weight
                "headcirc" -> target.headCircumference
                else -> null
            }

            val now = Calendar.getInstance()
            val then = Calendar.getInstance().apply { timeInMillis = target.date }
            val ageInMonths = floor((now.timeInMillis - then.timeInMillis) / (1000L * 60 * 60 * 24 * 30.44)).toFloat()

            val genderPref = EncryptedPrefs.getPrefs(this@MeasurementResultDetailActivity)
                .getString("gender", "boy") ?: "boy"
            val gender = if (genderPref == "boy") Gender.BOY else Gender.GIRL

            val type = when (parameter) {
                "height" -> MeasurementType.HEIGHT_AGE
                "weight" -> MeasurementType.WEIGHT_AGE
                "headcirc" -> MeasurementType.HEADCIRC_AGE
                else -> null
            }

            if (value != null && type != null) {
                val centile = CentileEvaluator.evaluateCentile(gender, type, ageInMonths, value)
                valueView.text = "Значение: $value"
                centileView.text = "Центиль: ${centile?.let { "%.1f".format(it) } ?: "Ошибка"}"
            } else {
                valueView.text = "Данные отсутствуют"
            }
        }

        findViewById<Button>(R.id.button_back_to_parameters).setOnClickListener {
            finish()
        }
    }

}
