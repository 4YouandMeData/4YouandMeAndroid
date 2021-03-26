package com.foryouandme.core.arch.navigation.action

import com.foryouandme.core.activity.FYAMActivity

fun authActivityAction(): ActivityAction =
    {
        (it as FYAMActivity).auth()
    }