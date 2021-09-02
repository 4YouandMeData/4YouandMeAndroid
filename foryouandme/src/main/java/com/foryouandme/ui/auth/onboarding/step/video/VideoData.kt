package com.foryouandme.ui.auth.onboarding.step.video

import com.foryouandme.core.arch.LazyData
import com.foryouandme.core.arch.toData
import com.foryouandme.entity.configuration.Configuration

data class VideoState(
    val configuration: LazyData<Configuration> = LazyData.Empty,
    val isPlaying: Boolean = false
) {

    companion object {

        fun mock(): VideoState =
            VideoState(
                configuration = Configuration.mock().toData(),
                isPlaying = false
            )

    }

}

sealed class VideoAction {

    object GetConfiguration : VideoAction()
    object Play: VideoAction()
    object Pause: VideoAction()


}