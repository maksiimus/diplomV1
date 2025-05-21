package com.example.diplomv1.presentation.measurement

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.diplomv1.R

class MeasurementResultsOverviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_measurement_results_overview)

        val childId = intent.getIntExtra("childId", -1)
        val measurementId = intent.getIntExtra("measurementId", -1) // добавлено

        val buttonMap = mapOf(
            R.id.buttonHeightResult to "height",
            R.id.buttonWeightResult to "weight",
            R.id.buttonHeadResult to "headcirc"
        )

        for ((buttonId, param) in buttonMap) {
            findViewById<Button>(buttonId).setOnClickListener {
                val intent = Intent(this, MeasurementResultDetailActivity::class.java)
                intent.putExtra("childId", childId)
                intent.putExtra("parameter", param)
                if (measurementId != -1) {
                    intent.putExtra("measurementId", measurementId) // передаём дальше
                }
                startActivity(intent)
            }
        }

        findViewById<Button>(R.id.button_back_to_measurements).setOnClickListener {
            finish()
        }
    }
}


