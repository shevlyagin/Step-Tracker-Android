package com.shevlyagin.steptracker.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shevlyagin.steptracker.ui.theme.StepTrackerAndroidTheme

@Composable
fun StatisticsScreen(
    selectedMonth: Int,
    totalSteps: Long,
    averageSteps: Double,
    maximumSteps: Int,
    bestStreak: Int,
    distanceInKilometers: Long,
    caloriesBurned: Long,
    onMonthChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Statistics",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Monthly activity overview",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        MonthSelectorCard(
            selectedMonth = selectedMonth,
            onMonthChange = onMonthChange
        )

        if (totalSteps == 0L) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text(
                    text = "No steps recorded for this month yet",
                    modifier = Modifier.padding(20.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        StatisticCard(
            label = "Total steps",
            value = formatWholeNumber(totalSteps),
            unit = "steps"
        )
        StatisticCard(
            label = "Average steps per day",
            value = formatAverage(averageSteps),
            unit = "steps"
        )
        StatisticCard(
            label = "Maximum steps in a day",
            value = formatWholeNumber(maximumSteps.toLong()),
            unit = "steps"
        )
        StatisticCard(
            label = "Best goal streak",
            value = formatWholeNumber(bestStreak.toLong()),
            unit = "days"
        )
        StatisticCard(
            label = "Distance",
            value = formatWholeNumber(distanceInKilometers),
            unit = "km"
        )
        StatisticCard(
            label = "Calories burned",
            value = formatWholeNumber(caloriesBurned),
            unit = "kcal"
        )
    }
}

@Composable
private fun MonthSelectorCard(
    selectedMonth: Int,
    onMonthChange: (Int) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Choose a month",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { onMonthChange(selectedMonth - 1) },
                    enabled = selectedMonth > 1,
                    modifier = Modifier.size(48.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(text = "−", style = MaterialTheme.typography.titleLarge)
                }

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Month",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = monthName(selectedMonth),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            softWrap = true
                        )
                    }
                }

                Button(
                    onClick = { onMonthChange(selectedMonth + 1) },
                    enabled = selectedMonth < 12,
                    modifier = Modifier.size(48.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(text = "+", style = MaterialTheme.typography.titleLarge)
                }
            }
        }
    }
}

@Composable
private fun StatisticCard(
    label: String,
    value: String,
    unit: String
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                softWrap = true
            )
            Text(
                text = unit,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun StatisticsScreenPreview() {
    StepTrackerAndroidTheme(dynamicColor = false) {
        Surface {
            StatisticsScreen(
                selectedMonth = 4,
                totalSteps = 356_780,
                averageSteps = 11_892.7,
                maximumSteps = 24_650,
                bestStreak = 12,
                distanceInKilometers = 267,
                caloriesBurned = 17_839,
                onMonthChange = {}
            )
        }
    }
}
