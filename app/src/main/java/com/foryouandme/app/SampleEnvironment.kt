package com.foryouandme.app

import com.foryouandme.core.arch.deps.Environment

class SampleEnvironment : Environment() {

    override fun isDebuggable(): Boolean = true

    override fun isStaging(): Boolean = true

    override fun studyId(): String = "bump"

}