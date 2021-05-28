package com.foryouandme.core.arch.android

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.arch.error.ErrorMessenger
import com.foryouandme.core.arch.error.ErrorView
import com.foryouandme.core.arch.navigation.Navigator
import com.foryouandme.core.arch.navigation.RootNavController
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.entity.configuration.Configuration
import javax.inject.Inject

abstract class BaseActivity : FragmentActivity {

    constructor() : super()
    constructor(contentLayoutId: Int) : super(contentLayoutId)

    @Inject
    lateinit var errorMessenger: ErrorMessenger

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var imageConfiguration: ImageConfiguration

    val name: String
        get() = javaClass.simpleName

    fun rootNavController(): RootNavController =
        RootNavController(supportFragmentManager.fragments[0].findNavController())

    /* --- error --- */

    protected fun ErrorView.setError(
        throwable: Throwable,
        configuration: Configuration?,
        retry: () -> Unit = {}
    ) {

        lifecycleScope.launchSafe {

            setError(
                errorMessenger.getTitle(),
                errorMessenger.getMessage(throwable, configuration),
                retry
            )


        }

    }

}