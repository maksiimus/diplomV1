package com.example.diplomv1.presentation.child

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.diplomv1.R
import com.example.diplomv1.data.db.AppDatabase
import com.example.diplomv1.data.model.Child
import com.example.diplomv1.utils.EncryptedPrefs
import kotlinx.coroutines.*
import java.util.*

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

        findViewById<Button>(R.id.buttonBackToMain).setOnClickListener {
            finish()
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

            val inflater = layoutInflater

            children.forEach { child: Child ->
                val card = inflater.inflate(R.layout.item_child_card, layout, false)

                val nameText = card.findViewById<TextView>(R.id.textChildName)
                val genderText = card.findViewById<TextView>(R.id.textChildGender)
                val ageText = card.findViewById<TextView>(R.id.textChildAge)
                val icon = card.findViewById<TextView>(R.id.iconChild)
                val historyButton = card.findViewById<Button>(R.id.buttonViewHistory)

                nameText.text = "${child.surname} ${child.name} ${child.patronymic}"
                genderText.text = "Пол: ${if (child.gender.lowercase() == "мальчик") "Мальчик" else "Девочка"}"
                icon.text = if (child.gender.lowercase() == "мальчик") "👦" else "👧"
                ageText.text = calculateAgeText(child.birthDate)

                historyButton.setOnClickListener {
                    val intent = Intent(this@ChildListActivity, com.example.diplomv1.presentation.measurement.MeasurementListActivity::class.java)
                    intent.putExtra("childId", child.id)
                    intent.putExtra("childName", "${child.surname} ${child.name}")
                    startActivity(intent)
                }

                layout.addView(card)
            }
        }
    }

    private fun calculateAgeText(birthMillis: Long): String {
        val birth = Calendar.getInstance().apply { timeInMillis = birthMillis }
        val now = Calendar.getInstance()

        var years = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR)
        var months = now.get(Calendar.MONTH) - birth.get(Calendar.MONTH)
        var days = now.get(Calendar.DAY_OF_MONTH) - birth.get(Calendar.DAY_OF_MONTH)

        if (days < 0) {
            months -= 1
            val prevMonth = (now.clone() as Calendar).apply { add(Calendar.MONTH, -1) }
            days += prevMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
        }

        if (months < 0) {
            years -= 1
            months += 12
        }

        return "Возраст: ${years} г. ${months} мес. ${days} дн."
    }
}
