package com.foryouandme.researchkit.task

interface TaskInjector {

    fun provideBuilder(): TaskConfiguration

}