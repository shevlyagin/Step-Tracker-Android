package com.shevlyagin.steptracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.shevlyagin.steptracker.ui.AddStepsScreen
import com.shevlyagin.steptracker.ui.theme.StepTrackerAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StepTrackerAndroidTheme {
                val stepTrackerState = rememberStepTrackerState()
                var selectedMonth by remember { mutableStateOf(1) }
                var selectedDay by remember { mutableStateOf(1) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AddStepsScreen(
                        selectedMonth = selectedMonth,
                        selectedDay = selectedDay,
                        savedSteps = stepTrackerState.getSteps(selectedMonth, selectedDay),
                        onMonthChange = { selectedMonth = it },
                        onDayChange = { selectedDay = it },
                        onSaveSteps = { steps ->
                            stepTrackerState.saveSteps(selectedMonth, selectedDay, steps)
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
