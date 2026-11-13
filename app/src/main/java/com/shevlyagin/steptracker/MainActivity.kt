package com.shevlyagin.steptracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.shevlyagin.steptracker.navigation.NavigationWrapper
import com.shevlyagin.steptracker.ui.theme.StepTrackerAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StepTrackerAndroidTheme {
                NavigationWrapper()
            }
        }
    }
}
