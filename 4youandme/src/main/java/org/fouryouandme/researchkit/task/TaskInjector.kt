package org.fouryouandme.researchkit.task

interface TaskInjector {

    fun provideBuilder(): TaskConfiguration

}