package com.foryouandme.app

import com.foryouandme.core.arch.app.ForYouAndMeApp
import com.foryouandme.data.datasource.Environment
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.arch.deps.VideoConfiguration
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SampleApp : ForYouAndMeApp() {

    override val environment: Environment
        get() = SampleEnvironment(this)

    override val imageConfiguration: ImageConfiguration
        get() = SampleImageConfiguration()

    override val videoConfiguration: VideoConfiguration
        get() = SampleVideoConfiguration()

    override val firebaseAnalytics: FirebaseAnalytics
        get() = FirebaseAnalytics.getInstance(this)


}