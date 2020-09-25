package org.fouryouandme.researchkit.task

abstract class TaskBuilder {

    abstract suspend fun buildByIdentifier(identifier: String): Task?

}