package org.fouryouandme.core.arch.navigation.execution

import org.fouryouandme.core.arch.navigation.NavigationExecution
import org.fouryouandme.tasks.step.StepFragmentDirections

fun stepToStep(index: Int): NavigationExecution = {
    it.navigate(StepFragmentDirections.actionStepToStep(index))
}