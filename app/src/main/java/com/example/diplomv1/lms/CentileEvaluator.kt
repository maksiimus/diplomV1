package com.example.diplomv1.lms

object CentileEvaluator {

    /**
     * Универсальный способ рассчитать центиль.
     * @param value измеренное значение (рост, вес и т.д.)
     * @param ageOrHeight возраст в месяцах или рост в см — зависит от параметра
     */
    fun evaluateCentile(
        gender: Gender,
        type: MeasurementType,
        ageOrHeight: Float,
        value: Float
    ): Float? {
        val lms = LMSRepository.getLMS(gender, type, ageOrHeight)
        return lms?.let { LMSCalculator.calculateCentile(value, it.l, it.m, it.s) }
    }
}