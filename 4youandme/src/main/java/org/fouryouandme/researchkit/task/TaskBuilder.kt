package org.fouryouandme.researchkit.task

abstract class TaskBuilder {

    abstract suspend fun build(type: String, id: String): Task?

}