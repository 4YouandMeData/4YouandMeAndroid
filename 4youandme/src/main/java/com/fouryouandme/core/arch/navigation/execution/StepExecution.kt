package com.fouryouandme.core.arch.navigation.execution

import com.fouryouandme.core.arch.navigation.NavigationExecution
import com.fouryouandme.researchkit.step.StepContainerFragmentDirections

fun stepToStep(index: Int): NavigationExecution = {
    it.navigate(StepContainerFragmentDirections.actionStepToStep(index))
}