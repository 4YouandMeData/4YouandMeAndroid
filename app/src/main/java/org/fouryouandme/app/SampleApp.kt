package org.fouryouandme.app

import com.google.firebase.analytics.FirebaseAnalytics
import org.fouryouandme.core.arch.app.FourYouAndMeApp
import org.fouryouandme.core.arch.deps.Environment
import org.fouryouandme.core.arch.deps.ImageConfiguration

class SampleApp : FourYouAndMeApp() {

    override val environment: Environment
        get() = SampleEnvironment()

    override val imageConfiguration: ImageConfiguration
        get() = SampleImageConfiguration()

    override val firebaseAnalytics: FirebaseAnalytics
        get() = FirebaseAnalytics.getInstance(this)


}