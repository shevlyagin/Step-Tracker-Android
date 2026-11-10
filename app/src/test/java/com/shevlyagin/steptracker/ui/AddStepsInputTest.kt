package com.shevlyagin.steptracker.ui

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AddStepsInputTest {
    @Test
    fun emptyInputIsRejected() {
        assertEquals("Enter the number of steps", validateStepsInput(""))
        assertEquals("Enter the number of steps", validateStepsInput("   "))
    }

    @Test
    fun negativeValueIsRejected() {
        assertEquals("Steps cannot be negative", validateStepsInput("-1"))
    }

    @Test
    fun lettersAndNonIntegerValuesAreRejected() {
        assertEquals(
            "Enter a whole number from 0 to ${Int.MAX_VALUE}",
            validateStepsInput("steps")
        )
        assertEquals(
            "Enter a whole number from 0 to ${Int.MAX_VALUE}",
            validateStepsInput("1.5")
        )
        assertEquals(
            "Enter a whole number from 0 to ${Int.MAX_VALUE}",
            validateStepsInput("2147483648")
        )
    }

    @Test
    fun zeroAndPositiveValuesAreAccepted() {
        assertNull(validateStepsInput("0"))
        assertNull(validateStepsInput("12500"))
        assertNull(validateStepsInput(Int.MAX_VALUE.toString()))
    }
}
