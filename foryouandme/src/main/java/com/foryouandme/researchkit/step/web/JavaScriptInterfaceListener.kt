package com.foryouandme.researchkit.step.web

interface JavaScriptInterfaceListener {

    fun nextStep(): Unit

    fun close(): Unit

}

abstract class WebStepInterface {

    private var listener: JavaScriptInterfaceListener? = null

    fun setListener(listener: JavaScriptInterfaceListener): Unit {
        this.listener = listener
    }

    fun clearListener(): Unit {
        listener = null
    }


    protected fun nextStep(): Unit {
        listener?.nextStep()
    }

    protected fun close(): Unit {
        listener?.close()
    }

}