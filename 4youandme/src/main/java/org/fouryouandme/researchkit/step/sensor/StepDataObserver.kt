package org.fouryouandme.researchkit.step.sensor

import arrow.core.Tuple2
import org.fouryouandme.researchkit.step.Step
import org.fouryouandme.researchkit.task.Task

data class StepDataObserver(val onNewData: (Tuple2<Step.SensorStep, Task>) -> Unit)

class StepDataObservable() {

    private val observers: MutableList<StepDataObserver> = mutableListOf()

    fun observe(observer: StepDataObserver): Unit {

        observers.add(observer)

    }

    fun notify(value: Tuple2<Step.SensorStep, Task>): Unit =
        observers.forEach { it.onNewData(value) }

    fun clear(): Unit = observers.clear()

}