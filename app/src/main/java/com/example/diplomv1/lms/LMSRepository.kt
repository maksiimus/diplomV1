package com.example.diplomv1.lms

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.math.abs

object LMSRepository {

    private val cache: MutableMap<String, List<LMS>> = mutableMapOf()

    fun init(context: Context) {
        for (gender in Gender.values()) {
            for (type in MeasurementType.values()) {
                val key = key(gender, type)
                val filePath = "${gender.folderName()}/${type.fileName}"
                val list = loadFromAssets(context, filePath)
                cache[key] = list
            }
        }
        println("LMSRepository cache loaded keys:")
        cache.keys.forEach { println("  • $it → ${cache[it]?.size ?: 0} записей") }
    }

    fun getLMS(gender: Gender, type: MeasurementType, target: Float): LMS? {
        val key = key(gender, type)
        val list = cache[key] ?: return null
        return list.minByOrNull { abs(it.age - target) }
    }

    private fun loadFromAssets(context: Context, filePath: String): List<LMS> {
        val result = mutableListOf<LMS>()
        val input = context.assets.open(filePath)
        val reader = BufferedReader(InputStreamReader(input))

        reader.readLine() // skip header
        reader.forEachLine { line ->
            val parts = line.split(";")
            if (parts.size >= 4) {
                val age = parts[0].replace(',','.').toFloatOrNull()
                val l = parts[1].replace(',','.').toFloatOrNull()
                val m = parts[2].replace(',','.').toFloatOrNull()
                val s = parts[3].replace(',','.').toFloatOrNull()
                if (age != null && l != null && m != null && s != null) {
                    result.add(LMS(age, l, m, s))
                }
            }
        }
        return result
    }

    private fun key(gender: Gender, type: MeasurementType): String {
        return "${gender.name}/${type.name}"
    }
}