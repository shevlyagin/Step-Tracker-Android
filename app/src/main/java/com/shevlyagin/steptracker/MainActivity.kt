package com.shevlyagin.steptracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.shevlyagin.steptracker.ui.GoalsScreen
import com.shevlyagin.steptracker.ui.theme.StepTrackerAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StepTrackerAndroidTheme {
                val stepTrackerState = rememberStepTrackerState()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GoalsScreen(
                        currentGoal = stepTrackerState.dailyGoal,
                        onSaveGoal = stepTrackerState::updateDailyGoal,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
