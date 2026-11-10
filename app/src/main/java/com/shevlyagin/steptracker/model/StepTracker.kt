package com.shevlyagin.steptracker.model

class StepTracker {
    private val stepsByMonth = List(MONTHS_PER_YEAR) { MutableList(DAYS_PER_MONTH) { 0 } }

    var dailyGoal: Int = 10_000
        private set

    fun setSteps(month: Int, day: Int, steps: Int) {
        validateMonth(month)
        require(day in 1..DAYS_PER_MONTH) { "Day must be between 1 and 30." }
        require(steps >= 0) { "Steps must not be negative." }
        stepsByMonth[month - 1][day - 1] = steps
    }

    fun getSteps(month: Int, day: Int): Int {
        validateMonth(month)
        require(day in 1..DAYS_PER_MONTH) { "Day must be between 1 and 30." }
        return stepsByMonth[month - 1][day - 1]
    }

    fun getMonthSteps(month: Int): List<Int> {
        validateMonth(month)
        return stepsByMonth[month - 1].toList()
    }

    fun updateDailyGoal(goal: Int) {
        require(goal > 0) { "Daily goal must be greater than zero." }
        dailyGoal = goal
    }

    private fun validateMonth(month: Int) {
        require(month in 1..MONTHS_PER_YEAR) { "Month must be between 1 and 12." }
    }

    companion object {
        const val MONTHS_PER_YEAR = 12
        const val DAYS_PER_MONTH = 30
    }
}
