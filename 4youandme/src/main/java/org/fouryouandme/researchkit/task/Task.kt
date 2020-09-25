package org.fouryouandme.researchkit.task

import org.fouryouandme.researchkit.step.Step

abstract class Task(val type: String, val id: String) {

    abstract val steps: List<Step>

}