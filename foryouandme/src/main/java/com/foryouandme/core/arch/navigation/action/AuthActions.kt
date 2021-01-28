package com.foryouandme.core.arch.navigation.action

import com.foryouandme.core.activity.FYAMActivity
import com.foryouandme.core.arch.navigation.ActivityAction

fun authActivityAction(): ActivityAction =
    {
        (it as FYAMActivity).auth()
    }