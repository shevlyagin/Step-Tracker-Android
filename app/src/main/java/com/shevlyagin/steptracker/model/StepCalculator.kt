package com.shevlyagin.steptracker.model

class StepCalculator {
    fun totalSteps(days: List<Int>): Long {
        validateDays(days)
        return days.sumOf { it.toLong() }
    }

    fun averageSteps(days: List<Int>): Double =
        totalSteps(days).toDouble() / StepTracker.DAYS_PER_MONTH

    fun maximumSteps(days: List<Int>): Int {
        validateDays(days)
        return days.max()
    }

    fun bestStreak(days: List<Int>, dailyGoal: Int): Int {
        validateDays(days)
        require(dailyGoal > 0) { "Daily goal must be greater than zero." }
        var currentStreak = 0
        var bestStreak = 0
        for (steps in days) {
            currentStreak = if (steps >= dailyGoal) currentStreak + 1 else 0
            bestStreak = maxOf(bestStreak, currentStreak)
        }
        return bestStreak
    }

    fun stepsToKilometers(steps: Long): Long {
        require(steps >= 0) { "Steps must not be negative." }
        // Preserve integer truncation from Java without overflowing steps * 75.
        return (steps / 100_000) * 75 + (steps % 100_000) * 75 / 100_000
    }

    fun stepsToKilocalories(steps: Long): Long {
        require(steps >= 0) { "Steps must not be negative." }
        return steps / 20
    }

    private fun validateDays(days: List<Int>) {
        require(days.size == StepTracker.DAYS_PER_MONTH) { "A month must contain 30 days." }
        require(days.all { it >= 0 }) { "Steps must not be negative." }
    }
}
