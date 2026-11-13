package com.shevlyagin.steptracker.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.shevlyagin.steptracker.rememberStepTrackerState
import com.shevlyagin.steptracker.ui.AddStepsScreen
import com.shevlyagin.steptracker.ui.DashboardScreen
import com.shevlyagin.steptracker.ui.GoalsScreen
import com.shevlyagin.steptracker.ui.StatisticsScreen

@Composable
fun NavigationWrapper(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(Routes.Dashboard)
    val stepTrackerState = rememberStepTrackerState()
    var selectedMonth by remember { mutableStateOf(1) }
    var selectedDay by remember { mutableStateOf(1) }
    val currentRoute = backStack.last() as Routes

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                BottomNavigationItem(
                    label = "Dashboard",
                    symbol = "D",
                    selected = currentRoute == Routes.Dashboard,
                    onClick = { navigateTo(backStack, Routes.Dashboard) }
                )
                BottomNavigationItem(
                    label = "Add Steps",
                    symbol = "+",
                    selected = currentRoute == Routes.AddSteps,
                    onClick = { navigateTo(backStack, Routes.AddSteps) }
                )
                BottomNavigationItem(
                    label = "Statistics",
                    symbol = "S",
                    selected = currentRoute == Routes.Statistics,
                    onClick = { navigateTo(backStack, Routes.Statistics) }
                )
                BottomNavigationItem(
                    label = "Goals",
                    symbol = "G",
                    selected = currentRoute == Routes.Goals,
                    onClick = { navigateTo(backStack, Routes.Goals) }
                )
            }
        }
    ) { innerPadding ->
        NavDisplay(
            backStack = backStack,
            modifier = Modifier.padding(innerPadding),
            onBack = {
                if (backStack.size > 1) backStack.removeLastOrNull()
            },
            entryProvider = entryProvider {
                entry<Routes.Dashboard> {
                    DashboardScreen(
                        steps = stepTrackerState.getSteps(selectedMonth, selectedDay),
                        dailyGoal = stepTrackerState.dailyGoal,
                        selectedMonth = selectedMonth,
                        selectedDay = selectedDay,
                        onMonthChange = { selectedMonth = it },
                        onDayChange = { selectedDay = it }
                    )
                }
                entry<Routes.AddSteps> {
                    AddStepsScreen(
                        selectedMonth = selectedMonth,
                        selectedDay = selectedDay,
                        savedSteps = stepTrackerState.getSteps(selectedMonth, selectedDay),
                        onMonthChange = { selectedMonth = it },
                        onDayChange = { selectedDay = it },
                        onSaveSteps = { steps ->
                            stepTrackerState.saveSteps(selectedMonth, selectedDay, steps)
                        }
                    )
                }
                entry<Routes.Statistics> {
                    StatisticsScreen(
                        selectedMonth = selectedMonth,
                        totalSteps = stepTrackerState.totalSteps(selectedMonth),
                        averageSteps = stepTrackerState.averageSteps(selectedMonth),
                        maximumSteps = stepTrackerState.maximumSteps(selectedMonth),
                        bestStreak = stepTrackerState.bestStreak(selectedMonth),
                        distanceInKilometers =
                            stepTrackerState.distanceInKilometers(selectedMonth),
                        caloriesBurned = stepTrackerState.caloriesBurned(selectedMonth),
                        onMonthChange = { selectedMonth = it }
                    )
                }
                entry<Routes.Goals> {
                    GoalsScreen(
                        currentGoal = stepTrackerState.dailyGoal,
                        onSaveGoal = stepTrackerState::updateDailyGoal
                    )
                }
            }
        )
    }
}

@Composable
private fun RowScope.BottomNavigationItem(
    label: String,
    symbol: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = { Text(symbol) },
        label = { Text(label) }
    )
}

internal fun navigateTo(backStack: MutableList<NavKey>, route: Routes) {
    if (backStack.lastOrNull() == route) return

    val existingIndex = backStack.indexOf(route)
    if (existingIndex >= 0) {
        while (backStack.lastIndex > existingIndex) {
            backStack.removeLastOrNull()
        }
    } else {
        backStack.add(route)
    }
}
