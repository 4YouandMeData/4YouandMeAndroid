package com.foryouandme.core.arch.deps.modules

import android.app.Application

data class PermissionModule(val application: Application, val analyticsModule: AnalyticsModule)