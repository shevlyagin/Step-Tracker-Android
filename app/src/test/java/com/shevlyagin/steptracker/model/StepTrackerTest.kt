package com.shevlyagin.steptracker.model

import org.junit.Assert.*
import org.junit.Test

class StepTrackerTest {
    @Test
    fun startsWithTwelveEmptyMonthsAndDefaultGoal() {
        val tracker = StepTracker()
        assertEquals(10_000, tracker.dailyGoal)
        for (month in 1..12) assertEquals(List(30) { 0 }, tracker.getMonthSteps(month))
    }

    @Test
    fun savesBoundaryDatesWithoutChangingOtherDaysOrMonths() {
        val tracker = StepTracker()
        tracker.setSteps(1, 1, 150)
        tracker.setSteps(12, 30, Int.MAX_VALUE)
        assertEquals(150, tracker.getSteps(1, 1))
        assertEquals(Int.MAX_VALUE, tracker.getSteps(12, 30))
        assertEquals(0, tracker.getSteps(1, 30))
        assertEquals(0, tracker.getSteps(12, 1))
        assertEquals(List(30) { 0 }, tracker.getMonthSteps(2))
    }

    @Test
    fun replacesExistingStepsAndAllowsResetToZero() {
        val tracker = StepTracker()
        tracker.setSteps(3, 5, 100)
        tracker.setSteps(3, 5, 200)
        assertEquals(200, tracker.getSteps(3, 5))
        tracker.setSteps(3, 5, 0)
        assertEquals(0, tracker.getSteps(3, 5))
    }

    @Test
    fun monthSnapshotDoesNotExposeStorage() {
        val tracker = StepTracker()
        val snapshot = tracker.getMonthSteps(1)
        tracker.setSteps(1, 1, 500)
        assertEquals(0, snapshot[0])
        (snapshot as MutableList<Int>)[1] = 999
        assertEquals(0, tracker.getSteps(1, 2))
    }

    @Test
    fun rejectsInvalidDatesForReadsAndWrites() {
        val tracker = StepTracker()
        for (month in listOf(Int.MIN_VALUE, 0, 13, Int.MAX_VALUE)) {
            assertThrows(IllegalArgumentException::class.java) { tracker.setSteps(month, 1, 1) }
            assertThrows(IllegalArgumentException::class.java) { tracker.getSteps(month, 1) }
            assertThrows(IllegalArgumentException::class.java) { tracker.getMonthSteps(month) }
        }
        for (day in listOf(Int.MIN_VALUE, 0, 31, Int.MAX_VALUE)) {
            assertThrows(IllegalArgumentException::class.java) { tracker.setSteps(1, day, 1) }
            assertThrows(IllegalArgumentException::class.java) { tracker.getSteps(1, day) }
        }
    }

    @Test
    fun rejectsNegativeStepsWithoutChangingExistingRecord() {
        val tracker = StepTracker()
        tracker.setSteps(1, 1, 500)
        for (steps in listOf(-1, Int.MIN_VALUE)) {
            assertThrows(IllegalArgumentException::class.java) { tracker.setSteps(1, 1, steps) }
        }
        assertEquals(500, tracker.getSteps(1, 1))
    }

    @Test
    fun acceptsPositiveGoalsAndRejectsOthersWithoutLosingGoal() {
        val tracker = StepTracker()
        tracker.updateDailyGoal(1)
        assertEquals(1, tracker.dailyGoal)
        tracker.updateDailyGoal(Int.MAX_VALUE)
        for (goal in listOf(0, -1, Int.MIN_VALUE)) {
            assertThrows(IllegalArgumentException::class.java) { tracker.updateDailyGoal(goal) }
        }
        assertEquals(Int.MAX_VALUE, tracker.dailyGoal)
    }

    @Test
    fun changingGoalRecalculatesStreakWithoutChangingSteps() {
        val tracker = StepTracker()
        val calculator = StepCalculator()
        tracker.setSteps(1, 1, 5_000)
        tracker.setSteps(1, 2, 10_000)
        assertEquals(1, calculator.bestStreak(tracker.getMonthSteps(1), tracker.dailyGoal))
        tracker.updateDailyGoal(5_000)
        assertEquals(2, calculator.bestStreak(tracker.getMonthSteps(1), tracker.dailyGoal))
        assertEquals(15_000L, calculator.totalSteps(tracker.getMonthSteps(1)))
    }
}
