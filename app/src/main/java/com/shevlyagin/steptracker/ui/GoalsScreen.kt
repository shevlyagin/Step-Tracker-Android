package com.shevlyagin.steptracker.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shevlyagin.steptracker.ui.theme.StepTrackerAndroidTheme

@Composable
fun GoalsScreen(
    currentGoal: Int,
    onSaveGoal: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var goalInput by remember(currentGoal) {
        mutableStateOf(currentGoal.toString())
    }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showSuccessMessage by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Goals",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Set the number of steps you want to reach every day",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Current daily goal",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = formatWholeNumber(currentGoal),
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "steps",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Change daily goal",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )

                TextField(
                    value = goalInput,
                    onValueChange = {
                        goalInput = it
                        errorMessage = null
                        showSuccessMessage = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("New daily goal") },
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
                        val validationMessage = validateGoalInput(goalInput)
                        if (validationMessage == null) {
                            onSaveGoal(goalInput.toInt())
                            errorMessage = null
                            showSuccessMessage = true
                        } else {
                            errorMessage = validationMessage
                            showSuccessMessage = false
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Goal")
                }

                if (showSuccessMessage) {
                    Text(
                        text = "Daily goal saved successfully",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

internal fun validateGoalInput(input: String): String? {
    if (input.isBlank()) return "Enter a daily goal"

    val goal = input.toIntOrNull()
        ?: return "Enter a whole number from 1 to ${formatWholeNumber(Int.MAX_VALUE)}"

    return if (goal <= 0) "Daily goal must be greater than zero" else null
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun GoalsScreenPreview() {
    StepTrackerAndroidTheme(dynamicColor = false) {
        Surface {
            GoalsScreen(
                currentGoal = 12_000,
                onSaveGoal = {}
            )
        }
    }
}
