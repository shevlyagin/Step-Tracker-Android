package com.shevlyagin.steptracker.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed class Routes : NavKey {
    @Serializable
    data object Dashboard : Routes()

    @Serializable
    data object AddSteps : Routes()

    @Serializable
    data object Statistics : Routes()

    @Serializable
    data object Goals : Routes()
}
