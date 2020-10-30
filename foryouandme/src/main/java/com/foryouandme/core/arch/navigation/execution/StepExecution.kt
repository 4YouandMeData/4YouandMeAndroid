package com.foryouandme.core.arch.navigation.execution

import com.foryouandme.core.arch.navigation.NavigationExecution
import com.foryouandme.researchkit.step.StepContainerFragmentDirections

fun stepToStep(index: Int): NavigationExecution = {
    it.navigate(StepContainerFragmentDirections.actionStepToStep(index))
}