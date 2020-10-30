package com.foryouandme.app

import com.foryouandme.core.arch.app.ForYouAndMeApp
import com.foryouandme.core.arch.deps.Environment
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.google.firebase.analytics.FirebaseAnalytics

class SampleApp : ForYouAndMeApp() {

    override val environment: Environment
        get() = SampleEnvironment()

    override val imageConfiguration: ImageConfiguration
        get() = SampleImageConfiguration()

    override val firebaseAnalytics: FirebaseAnalytics
        get() = FirebaseAnalytics.getInstance(this)


}