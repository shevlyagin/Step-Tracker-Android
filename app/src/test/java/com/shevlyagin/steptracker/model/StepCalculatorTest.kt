package com.shevlyagin.steptracker.model

import org.junit.Assert.*
import org.junit.Test

class StepCalculatorTest {
    private val calculator = StepCalculator()

    @Test
    fun emptyMonthHasZeroStatistics() {
        val days = List(30) { 0 }
        assertEquals(0L, calculator.totalSteps(days))
        assertEquals(0.0, calculator.averageSteps(days), 0.0)
        assertEquals(0, calculator.maximumSteps(days))
        assertEquals(0, calculator.bestStreak(days, 10_000))
    }

    @Test
    fun calculatesTotalMaximumAndAverageAcrossAllThirtyDays() {
        val days = MutableList(30) { 0 }
        days[0] = 1_000
        days[29] = 2_001
        assertEquals(3_001L, calculator.totalSteps(days))
        assertEquals(2_001, calculator.maximumSteps(days))
        assertEquals(3_001.0 / 30, calculator.averageSteps(days), 0.000001)
    }

    @Test
    fun largeMonthlyValuesDoNotOverflow() {
        val days = List(30) { Int.MAX_VALUE }
        assertEquals(64_424_509_410L, calculator.totalSteps(days))
        assertEquals(Int.MAX_VALUE.toDouble(), calculator.averageSteps(days), 0.0)
        assertEquals(Int.MAX_VALUE, calculator.maximumSteps(days))
        assertEquals(30, calculator.bestStreak(days, Int.MAX_VALUE))
        assertEquals(48_318_382L, calculator.stepsToKilometers(calculator.totalSteps(days)))
        assertEquals(3_221_225_470L, calculator.stepsToKilocalories(calculator.totalSteps(days)))
    }

    @Test
    fun streakIncludesEqualityResetsOnMissAndCountsMonthEnd() {
        val days = MutableList(30) { 0 }
        days[0] = 100
        days[1] = 101
        days[2] = 99
        days[27] = 100
        days[28] = 200
        days[29] = 100
        assertEquals(3, calculator.bestStreak(days, 100))
    }

    @Test
    fun streakDoesNotJoinMonthEndToMonthStart() {
        val days = MutableList(30) { 0 }
        days[0] = 100
        days[29] = 100
        assertEquals(1, calculator.bestStreak(days, 100))
    }

    @Test
    fun allDaysBelowGoalHaveNoStreak() {
        assertEquals(0, calculator.bestStreak(List(30) { 99 }, 100))
    }

    @Test
    fun conversionsPreserveJavaCoefficientsAndIntegerTruncation() {
        for ((steps, kilometers) in listOf(0L to 0L, 1_333L to 0L, 1_334L to 1L, 10_000L to 7L, 100_000L to 75L)) {
            assertEquals(kilometers, calculator.stepsToKilometers(steps))
        }
        for ((steps, calories) in listOf(0L to 0L, 19L to 0L, 20L to 1L, 39L to 1L, 10_000L to 500L)) {
            assertEquals(calories, calculator.stepsToKilocalories(steps))
        }
    }

    @Test
    fun conversionsSupportLongBoundaryWithoutOverflow() {
        assertEquals(6_917_529_027_641_081L, calculator.stepsToKilometers(Long.MAX_VALUE))
        assertEquals(461_168_601_842_738_790L, calculator.stepsToKilocalories(Long.MAX_VALUE))
    }

    @Test
    fun rejectsInvalidMonthCollectionsForEveryStatistic() {
        val invalidMonths = listOf(emptyList(), List(29) { 0 }, List(31) { 0 }, List(30) { -1 })
        for (days in invalidMonths) {
            assertThrows(IllegalArgumentException::class.java) { calculator.totalSteps(days) }
            assertThrows(IllegalArgumentException::class.java) { calculator.averageSteps(days) }
            assertThrows(IllegalArgumentException::class.java) { calculator.maximumSteps(days) }
            assertThrows(IllegalArgumentException::class.java) { calculator.bestStreak(days, 1) }
        }
    }

    @Test
    fun rejectsNonPositiveGoalsAndNegativeConversionInputs() {
        for (goal in listOf(0, -1, Int.MIN_VALUE)) {
            assertThrows(IllegalArgumentException::class.java) { calculator.bestStreak(List(30) { 0 }, goal) }
        }
        for (steps in listOf(-1L, Long.MIN_VALUE)) {
            assertThrows(IllegalArgumentException::class.java) { calculator.stepsToKilometers(steps) }
            assertThrows(IllegalArgumentException::class.java) { calculator.stepsToKilocalories(steps) }
        }
    }
}
