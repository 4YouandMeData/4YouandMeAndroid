package com.foryouandme.researchkit.task

import com.foryouandme.researchkit.step.Step

abstract class Task(val type: String, val id: String) {

    abstract val steps: List<Step>

}