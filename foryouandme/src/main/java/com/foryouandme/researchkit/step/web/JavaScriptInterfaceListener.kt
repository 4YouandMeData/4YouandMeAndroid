package com.foryouandme.researchkit.step.web

interface JavaScriptInterfaceListener {

    fun nextStep()

    fun close()

}

abstract class WebStepInterface {

    private var listener: JavaScriptInterfaceListener? = null

    fun setListener(listener: JavaScriptInterfaceListener) {
        this.listener = listener
    }

    fun clearListener() {
        listener = null
    }


    protected fun nextStep() {
        listener?.nextStep()
    }

    protected fun close() {
        listener?.close()
    }

}