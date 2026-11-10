package com.shevlyagin.steptracker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.shevlyagin.steptracker.model.StepCalculator
import com.shevlyagin.steptracker.model.StepTracker

class StepTrackerState {
    private val stepTracker = StepTracker()
    private val stepCalculator = StepCalculator()

    private var stepsByMonth by mutableStateOf(readAllSteps())

    var dailyGoal by mutableStateOf(stepTracker.dailyGoal)
        private set

    fun saveSteps(month: Int, day: Int, steps: Int) {
        stepTracker.setSteps(month, day, steps)
        stepsByMonth = readAllSteps()
    }

    fun updateDailyGoal(goal: Int) {
        stepTracker.updateDailyGoal(goal)
        dailyGoal = stepTracker.dailyGoal
    }

    fun getSteps(month: Int, day: Int): Int {
        stepTracker.getSteps(month, day)
        return stepsByMonth[month - 1][day - 1]
    }

    fun getMonthSteps(month: Int): List<Int> {
        stepTracker.getMonthSteps(month)
        return stepsByMonth[month - 1].toList()
    }

    fun totalSteps(month: Int): Long =
        stepCalculator.totalSteps(getMonthSteps(month))

    fun averageSteps(month: Int): Double =
        stepCalculator.averageSteps(getMonthSteps(month))

    fun maximumSteps(month: Int): Int =
        stepCalculator.maximumSteps(getMonthSteps(month))

    fun bestStreak(month: Int): Int =
        stepCalculator.bestStreak(getMonthSteps(month), dailyGoal)

    fun distanceInKilometers(month: Int): Long =
        stepCalculator.stepsToKilometers(totalSteps(month))

    fun caloriesBurned(month: Int): Long =
        stepCalculator.stepsToKilocalories(totalSteps(month))

    private fun readAllSteps(): List<List<Int>> =
        (1..StepTracker.MONTHS_PER_YEAR).map(stepTracker::getMonthSteps)
}

@Composable
fun rememberStepTrackerState(): StepTrackerState = remember { StepTrackerState() }
