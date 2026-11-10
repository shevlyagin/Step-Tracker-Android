package com.shevlyagin.steptracker

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class StepTrackerStateTest {
    @Test
    fun startsWithEmptyStepsAndDefaultGoal() {
        val state = StepTrackerState()

        assertEquals(10_000, state.dailyGoal)
        assertEquals(List(30) { 0 }, state.getMonthSteps(1))
        assertEquals(0, state.getSteps(12, 30))
    }

    @Test
    fun savingStepsUpdatesDayAndMonthlyStatistics() {
        val state = StepTrackerState()

        state.saveSteps(month = 2, day = 1, steps = 10_000)
        state.saveSteps(month = 2, day = 2, steps = 20_000)

        assertEquals(10_000, state.getSteps(2, 1))
        assertEquals(30_000L, state.totalSteps(2))
        assertEquals(1_000.0, state.averageSteps(2), 0.0)
        assertEquals(20_000, state.maximumSteps(2))
        assertEquals(2, state.bestStreak(2))
        assertEquals(22L, state.distanceInKilometers(2))
        assertEquals(1_500L, state.caloriesBurned(2))
    }

    @Test
    fun savingStepsAgainReplacesPreviousValue() {
        val state = StepTrackerState()
        state.saveSteps(month = 4, day = 10, steps = 8_000)

        state.saveSteps(month = 4, day = 10, steps = 12_000)

        assertEquals(12_000, state.getSteps(4, 10))
        assertEquals(12_000L, state.totalSteps(4))
    }

    @Test
    fun updatingGoalChangesGoalAndGoalBasedStatistics() {
        val state = StepTrackerState()
        state.saveSteps(month = 1, day = 1, steps = 5_000)
        state.saveSteps(month = 1, day = 2, steps = 7_000)
        assertEquals(0, state.bestStreak(1))

        state.updateDailyGoal(5_000)

        assertEquals(5_000, state.dailyGoal)
        assertEquals(2, state.bestStreak(1))
    }

    @Test
    fun monthsRemainIndependent() {
        val state = StepTrackerState()
        state.saveSteps(month = 1, day = 1, steps = 100)
        state.saveSteps(month = 12, day = 30, steps = 200)

        assertEquals(100L, state.totalSteps(1))
        assertEquals(0L, state.totalSteps(2))
        assertEquals(200L, state.totalSteps(12))
    }

    @Test
    fun invalidUpdatesAreRejectedWithoutChangingState() {
        val state = StepTrackerState()
        state.saveSteps(month = 1, day = 1, steps = 500)

        assertThrows(IllegalArgumentException::class.java) {
            state.saveSteps(month = 13, day = 1, steps = 100)
        }
        assertThrows(IllegalArgumentException::class.java) {
            state.saveSteps(month = 1, day = 31, steps = 100)
        }
        assertThrows(IllegalArgumentException::class.java) {
            state.saveSteps(month = 1, day = 1, steps = -1)
        }
        assertThrows(IllegalArgumentException::class.java) {
            state.updateDailyGoal(0)
        }

        assertEquals(500, state.getSteps(1, 1))
        assertEquals(10_000, state.dailyGoal)
    }
}
