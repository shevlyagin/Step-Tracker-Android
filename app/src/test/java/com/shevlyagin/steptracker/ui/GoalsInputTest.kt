package com.shevlyagin.steptracker.ui

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class GoalsInputTest {
    @Test
    fun emptyInputIsRejected() {
        assertEquals("Enter a daily goal", validateGoalInput(""))
        assertEquals("Enter a daily goal", validateGoalInput("   "))
    }

    @Test
    fun zeroAndNegativeValuesAreRejected() {
        assertEquals("Daily goal must be greater than zero", validateGoalInput("0"))
        assertEquals("Daily goal must be greater than zero", validateGoalInput("-1"))
    }

    @Test
    fun lettersFractionsAndOverflowAreRejected() {
        val expectedMessage =
            "Enter a whole number from 1 to ${formatWholeNumber(Int.MAX_VALUE)}"

        assertEquals(expectedMessage, validateGoalInput("goal"))
        assertEquals(expectedMessage, validateGoalInput("10000.5"))
        assertEquals(expectedMessage, validateGoalInput("2147483648"))
    }

    @Test
    fun positiveValuesAreAccepted() {
        assertNull(validateGoalInput("1"))
        assertNull(validateGoalInput("10000"))
        assertNull(validateGoalInput(Int.MAX_VALUE.toString()))
    }
}
