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
import com.shevlyagin.steptracker.ui.StatisticsScreen
import com.shevlyagin.steptracker.ui.theme.StepTrackerAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StepTrackerAndroidTheme {
                val stepTrackerState = rememberStepTrackerState()
                var selectedMonth by remember { mutableStateOf(1) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    StatisticsScreen(
                        selectedMonth = selectedMonth,
                        totalSteps = stepTrackerState.totalSteps(selectedMonth),
                        averageSteps = stepTrackerState.averageSteps(selectedMonth),
                        maximumSteps = stepTrackerState.maximumSteps(selectedMonth),
                        bestStreak = stepTrackerState.bestStreak(selectedMonth),
                        distanceInKilometers = stepTrackerState.distanceInKilometers(selectedMonth),
                        caloriesBurned = stepTrackerState.caloriesBurned(selectedMonth),
                        onMonthChange = { selectedMonth = it },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
