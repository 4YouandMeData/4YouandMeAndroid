package org.fouryouandme.app

import org.fouryouandme.core.arch.deps.Environment

class SampleEnvironment: Environment() {

    override fun isDebuggable(): Boolean = true

    override fun isStaging(): Boolean = true

    override fun studyId(): String = "bump"

}