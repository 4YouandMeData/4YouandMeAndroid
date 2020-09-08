package org.fouryouandme.core.arch.navigation.execution

import org.fouryouandme.core.arch.navigation.NavigationExecution
import org.fouryouandme.researchkit.step.StepContainerFragmentDirections

fun stepToStep(index: Int): NavigationExecution = {
    it.navigate(StepContainerFragmentDirections.actionStepToStep(index))
}