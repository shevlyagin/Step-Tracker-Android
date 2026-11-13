package com.shevlyagin.steptracker.navigation

import androidx.navigation3.runtime.NavKey
import org.junit.Assert.assertEquals
import org.junit.Test

class NavigationWrapperTest {
    @Test
    fun selectingCurrentRouteDoesNotCreateDuplicate() {
        val backStack = mutableListOf<NavKey>(Routes.Dashboard)

        navigateTo(backStack, Routes.Dashboard)

        assertEquals(listOf(Routes.Dashboard), backStack)
    }

    @Test
    fun selectingNewRoutesBuildsBackHistory() {
        val backStack = mutableListOf<NavKey>(Routes.Dashboard)

        navigateTo(backStack, Routes.AddSteps)
        navigateTo(backStack, Routes.Statistics)
        navigateTo(backStack, Routes.Goals)

        assertEquals(
            listOf(
                Routes.Dashboard,
                Routes.AddSteps,
                Routes.Statistics,
                Routes.Goals
            ),
            backStack
        )
    }

    @Test
    fun selectingVisitedRouteReturnsToItWithoutDuplicates() {
        val backStack = mutableListOf<NavKey>(
            Routes.Dashboard,
            Routes.AddSteps,
            Routes.Statistics,
            Routes.Goals
        )

        navigateTo(backStack, Routes.AddSteps)

        assertEquals(listOf(Routes.Dashboard, Routes.AddSteps), backStack)
    }

    @Test
    fun selectingDashboardReturnsToRoot() {
        val backStack = mutableListOf<NavKey>(
            Routes.Dashboard,
            Routes.AddSteps,
            Routes.Statistics
        )

        navigateTo(backStack, Routes.Dashboard)

        assertEquals(listOf(Routes.Dashboard), backStack)
    }
}
