package com.foryouandme.core.arch.deps.modules

import android.app.Application

data class DailySurveyTimeModule (val application: Application, val analyticsModule: AnalyticsModule)