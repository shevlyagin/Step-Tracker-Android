package com.shevlyagin.steptracker.ui

import java.text.NumberFormat

private val monthNames = listOf(
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December"
)

internal fun monthName(month: Int): String = monthNames[month - 1]

internal fun formatWholeNumber(value: Number): String =
    NumberFormat.getIntegerInstance().format(value)

internal fun formatAverage(value: Double): String =
    NumberFormat.getNumberInstance().apply {
        minimumFractionDigits = 1
        maximumFractionDigits = 1
    }.format(value)
