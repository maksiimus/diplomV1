package com.example.diplomv1.presentation.child

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.diplomv1.R
import com.example.diplomv1.data.db.AppDatabase
import com.example.diplomv1.data.model.Child
import com.example.diplomv1.utils.EncryptedPrefs
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class AddChildActivity : AppCompatActivity() {

    private val scope = CoroutineScope(Dispatchers.Main + Job())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_child)

        val surnameField = findViewById<EditText>(R.id.editSurname)
        val nameField = findViewById<EditText>(R.id.editName)
        val patronymicField = findViewById<EditText>(R.id.editPatronymic)
        val dateField = findViewById<EditText>(R.id.editBirthDate)
        val genderGroup = findViewById<RadioGroup>(R.id.genderGroup)
        val button = findViewById<Button>(R.id.buttonSave)

        dateField.setOnClickListener {
            val calendar = Calendar.getInstance()
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
            val surname = surnameField.text.toString()
            val name = nameField.text.toString()
            val patronymic = patronymicField.text.toString()
            val dateStr = dateField.text.toString()
            val genderId = genderGroup.checkedRadioButtonId

            if (surname.isBlank() || name.isBlank() || dateStr.isBlank() || genderId == -1) {
                Toast.makeText(this, getString(R.string.fill_required_fields), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val gender = findViewById<RadioButton>(genderId).text.toString()
            val birthDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).parse(dateStr)?.time ?: 0L
            val userId = EncryptedPrefs.getUserId(this)

            if (userId == -1) {
                Toast.makeText(this, "Ошибка: пользователь не найден", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val child = Child(
                userId = userId,
                surname = surname,
                name = name,
                patronymic = patronymic,
                birthDate = birthDate,
                gender = gender
            )

            scope.launch {
                val db = AppDatabase.getInstance(applicationContext)
                withContext(Dispatchers.IO) {
                    db.childDao().insert(child)
                }
                Toast.makeText(this@AddChildActivity, getString(R.string.child_added), Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
