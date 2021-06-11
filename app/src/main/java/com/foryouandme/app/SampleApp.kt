package com.foryouandme.app

import com.foryouandme.core.arch.app.ForYouAndMeApp
import com.foryouandme.data.datasource.Environment
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.arch.deps.VideoConfiguration
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SampleApp : ForYouAndMeApp()