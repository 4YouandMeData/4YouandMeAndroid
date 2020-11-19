package com.foryouandme.core.arch.navigation.execution

import com.foryouandme.auth.video.VideoFragmentDirections
import com.foryouandme.core.arch.navigation.NavigationExecution

fun videoToScreening(): NavigationExecution = {

    it.navigate(VideoFragmentDirections.actionVideoToScreening())

}