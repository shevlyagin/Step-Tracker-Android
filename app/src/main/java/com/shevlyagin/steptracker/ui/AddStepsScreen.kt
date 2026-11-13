package com.shevlyagin.steptracker.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shevlyagin.steptracker.ui.theme.StepTrackerAndroidTheme

@Composable
fun AddStepsScreen(
    selectedMonth: Int,
    selectedDay: Int,
    savedSteps: Int,
    onMonthChange: (Int) -> Unit,
    onDayChange: (Int) -> Unit,
    onSaveSteps: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var stepsInput by remember(selectedMonth, selectedDay, savedSteps) {
        mutableStateOf(savedSteps.toString())
    }
    var errorMessage by remember(selectedMonth, selectedDay) {
        mutableStateOf<String?>(null)
    }
    var showSuccessMessage by remember(selectedMonth, selectedDay) {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Add Steps",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Enter the total number of steps for a day",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        DateSelectorCard(
            selectedMonth = selectedMonth,
            selectedDay = selectedDay,
            onMonthChange = onMonthChange,
            onDayChange = onDayChange
        )

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Steps for ${monthName(selectedMonth)}, " +
                        "day ${formatWholeNumber(selectedDay)}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Currently saved: ${formatWholeNumber(savedSteps)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                TextField(
                    value = stepsInput,
                    onValueChange = {
                        stepsInput = it
                        errorMessage = null
                        showSuccessMessage = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Number of steps") },
                    singleLine = true,
                    isError = errorMessage != null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                if (errorMessage != null) {
                    Text(
                        text = errorMessage.orEmpty(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Button(
                    onClick = {
                        val validationMessage = validateStepsInput(stepsInput)
                        if (validationMessage == null) {
                            onSaveSteps(stepsInput.toInt())
                            errorMessage = null
                            showSuccessMessage = true
                        } else {
                            errorMessage = validationMessage
                            showSuccessMessage = false
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Steps")
                }

                if (showSuccessMessage) {
                    Text(
                        text = "Steps saved successfully",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun DateSelectorCard(
    selectedMonth: Int,
    selectedDay: Int,
    onMonthChange: (Int) -> Unit,
    onDayChange: (Int) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Choose a date",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            ValueSelector(
                label = "Month",
                value = monthName(selectedMonth),
                canDecrease = selectedMonth > 1,
                canIncrease = selectedMonth < 12,
                onDecrease = { onMonthChange(selectedMonth - 1) },
                onIncrease = { onMonthChange(selectedMonth + 1) }
            )
            ValueSelector(
                label = "Day",
                value = formatWholeNumber(selectedDay),
                canDecrease = selectedDay > 1,
                canIncrease = selectedDay < 30,
                onDecrease = { onDayChange(selectedDay - 1) },
                onIncrease = { onDayChange(selectedDay + 1) }
            )
        }
    }
}

@Composable
private fun ValueSelector(
    label: String,
    value: String,
    canDecrease: Boolean,
    canIncrease: Boolean,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = onDecrease,
            enabled = canDecrease,
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
                    text = label,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    softWrap = true
                )
            }
        }

        Button(
            onClick = onIncrease,
            enabled = canIncrease,
            modifier = Modifier.size(48.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(text = "+", style = MaterialTheme.typography.titleLarge)
        }
    }
}

internal fun validateStepsInput(input: String): String? {
    if (input.isBlank()) return "Enter the number of steps"

    val steps = input.toIntOrNull()
        ?: return "Enter a whole number from 0 to ${formatWholeNumber(Int.MAX_VALUE)}"

    return if (steps < 0) "Steps cannot be negative" else null
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AddStepsScreenPreview() {
    StepTrackerAndroidTheme(dynamicColor = false) {
        Surface {
            AddStepsScreen(
                selectedMonth = 4,
                selectedDay = 12,
                savedSteps = 6_500,
                onMonthChange = {},
                onDayChange = {},
                onSaveSteps = {}
            )
        }
    }
}
