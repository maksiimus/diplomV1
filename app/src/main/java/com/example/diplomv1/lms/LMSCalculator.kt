package com.example.diplomv1.lms

import kotlin.math.*

object LMSCalculator {

    fun calculateZ(x: Float, l: Float, m: Float, s: Float): Float {
        return if (l == 0f) {
            (ln(x / m) / s).toFloat()
        } else {
            (((x / m).toDouble().pow(l.toDouble()) - 1) / (l * s)).toFloat()
        }
    }

    fun calculateCentile(z: Float): Float {
        val phi = 0.5f * (1.0f + approximateErf(z / sqrt(2f)))
        return (phi * 100f).coerceIn(0f, 100f)
    }

    fun calculateCentile(x: Float, l: Float, m: Float, s: Float): Float {
        val z = calculateZ(x, l, m, s)
        return calculateCentile(z)
    }

    private fun approximateErf(z: Float): Float {
        val t = 1.0f / (1.0f + 0.3275911f * abs(z))
        val a1 = 0.254829592f
        val a2 = -0.284496736f
        val a3 = 1.421413741f
        val a4 = -1.453152027f
        val a5 = 1.061405429f

        val expTerm = exp(-z * z)
        val poly = (((((a5 * t + a4) * t + a3) * t + a2) * t + a1) * t)
        val erf = 1.0f - poly * expTerm

        return if (z >= 0) erf else -erf
    }
}
