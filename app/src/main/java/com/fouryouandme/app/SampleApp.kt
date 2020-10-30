package com.fouryouandme.app

import com.fouryouandme.core.arch.app.FourYouAndMeApp
import com.fouryouandme.core.arch.deps.Environment
import com.fouryouandme.core.arch.deps.ImageConfiguration
import com.google.firebase.analytics.FirebaseAnalytics

class SampleApp : FourYouAndMeApp() {

    override val environment: Environment
        get() = SampleEnvironment()

    override val imageConfiguration: ImageConfiguration
        get() = SampleImageConfiguration()

    override val firebaseAnalytics: FirebaseAnalytics
        get() = FirebaseAnalytics.getInstance(this)


}