package com.foryouandme.auth.video

import com.foryouandme.R
import com.foryouandme.core.arch.android.BaseFragment
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.core.ext.navigator

class VideoFragment : BaseFragment<VideoViewModel>(R.layout.video) {

    override val viewModel: VideoViewModel by lazy {

        viewModelFactory(
            this,
            getFactory { VideoViewModel(navigator) }
        )

    }

}