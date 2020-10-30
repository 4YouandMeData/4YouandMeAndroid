package com.fouryouandme.researchkit.task

import com.fouryouandme.researchkit.step.Step

abstract class Task(val type: String, val id: String) {

    abstract val steps: List<Step>

}