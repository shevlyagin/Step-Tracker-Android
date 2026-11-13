package com.shevlyagin.steptracker.ui

import java.util.Locale
import org.junit.Assert.assertEquals
import org.junit.Test

class UiFormattingTest {
    @Test
    fun monthNamesUseTheSameEnglishFormatAcrossScreens() {
        assertEquals("January", monthName(1))
        assertEquals("June", monthName(6))
        assertEquals("December", monthName(12))
    }

    @Test
    fun wholeNumbersUseTheDeviceLocale() = withDefaultLocale(Locale.GERMANY) {
        assertEquals("1.234.567", formatWholeNumber(1_234_567))
    }

    @Test
    fun averagesUseTheDeviceLocaleAndOneDecimalPlace() =
        withDefaultLocale(Locale.GERMANY) {
            assertEquals("12.345,7", formatAverage(12_345.67))
        }

    private fun withDefaultLocale(locale: Locale, block: () -> Unit) {
        val previousLocale = Locale.getDefault()
        try {
            Locale.setDefault(locale)
            block()
        } finally {
            Locale.setDefault(previousLocale)
        }
    }
}
