package org.fouryouandme.app

import org.fouryouandme.core.arch.deps.Environment
import org.fouryouandme.core.arch.deps.ImageConfiguration
import org.fouryouandme.core.arch.app.FourYouAndMeApp

class SampleApp : FourYouAndMeApp() {

    override val environment: Environment
        get() = SampleEnvironment()

    override val imageConfiguration: ImageConfiguration
        get() = SampleImageConfiguration()

}