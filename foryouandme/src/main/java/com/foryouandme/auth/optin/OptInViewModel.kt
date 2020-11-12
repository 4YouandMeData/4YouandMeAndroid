package com.foryouandme.auth.optin

import arrow.core.Either
import arrow.core.getOrElse
import arrow.core.left
import arrow.core.right
import com.foryouandme.auth.AuthNavController
import com.foryouandme.core.arch.android.BaseViewModel
import com.foryouandme.core.arch.deps.modules.OptInModule
import com.foryouandme.core.arch.deps.modules.PermissionModule
import com.foryouandme.core.arch.deps.modules.nullToError
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.arch.error.handleAuthError
import com.foryouandme.core.arch.navigation.*
import com.foryouandme.core.cases.optins.OptInsUseCase.getOptIns
import com.foryouandme.core.cases.optins.OptInsUseCase.setPermission
import com.foryouandme.core.cases.permission.PermissionUseCase.requestPermission
import com.foryouandme.core.cases.permission.PermissionUseCase.requestPermissions
import com.foryouandme.core.entity.configuration.Configuration
import com.foryouandme.core.ext.mapNotNull
import com.foryouandme.core.ext.startCoroutineAsync
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener
import com.karumi.dexter.listener.single.BasePermissionListener


class OptInViewModel(
    navigator: Navigator,
    private val optInModule: OptInModule,
    private val permissionModule: PermissionModule
) : BaseViewModel<
        OptInState,
        OptInStateUpdate,
        OptInError,
        OptInLoading>
    (navigator = navigator) {

    /* --- data --- */

    suspend fun initialize(
        rootNavController: RootNavController
    ): Either<ForYouAndMeError, OptInState> {

        showLoading(OptInLoading.Initialization)

        val state =
            optInModule.getOptIns()
                .nullToError()
                .handleAuthError(rootNavController, navigator)
                .fold(
                    {
                        setError(it, OptInError.Initialization)
                        it.left()
                    },
                    { optIns ->

                        val state = OptInState(optIns, emptyMap())

                        setState(state) { OptInStateUpdate.Initialization(it.optIns) }

                        state.right()
                    }
                )

        hideLoading(OptInLoading.Initialization)

        return state

    }

    /* --- permission --- */

    private suspend fun setPermission(
        rootNavController: RootNavController,
        optInNavController: OptInNavController,
        index: Int,
        permissionId: String,
        agree: Boolean
    ): Unit {

        showLoading(OptInLoading.PermissionSet)

        optInModule.setPermission(permissionId, agree)
            .handleAuthError(rootNavController, navigator).fold(
                { setError(it, OptInError.PermissionSet) },
                { nextPermission(optInNavController, index) }
            )

        hideLoading(OptInLoading.PermissionSet)

    }

    suspend fun requestPermissions(
        configuration: Configuration,
        rootNavController: RootNavController,
        optInNavController: OptInNavController,
        index: Int
    ): Unit {

        mapNotNull(
            state().permissions[index],
            state().optIns.permissions.getOrNull(index)
        )
            ?.let { (agree, optIn) ->

                if (agree) {

                    when (optIn.systemPermissions.size) {
                        0 ->
                            setPermission(
                                rootNavController,
                                optInNavController,
                                index,
                                optIn.id,
                                agree
                            )
                        1 -> {
                            permissionModule.requestPermission(optIn.systemPermissions[0])
                            setPermission(
                                rootNavController,
                                optInNavController,
                                index,
                                optIn.id,
                                agree
                            )
                        }
                        else -> {
                            permissionModule.requestPermissions(optIn.systemPermissions)
                            setPermission(
                                rootNavController,
                                optInNavController,
                                index,
                                optIn.id,
                                agree
                            )
                        }
                    }

                } else {

                    if (optIn.mandatory)
                        alert(
                            configuration.text.onboarding.optIn.mandatoryTitle,
                            optIn.mandatoryDescription
                                .getOrElse { configuration.text.onboarding.optIn.mandatoryDefault },
                            configuration.text.onboarding.optIn.mandatoryClose
                        )
                    else nextPermission(optInNavController, index)
                }
            }

    }

    suspend fun setPermissionState(id: Int, agree: Boolean): Unit {

        val map =
            state().permissions.toMutableMap().also { it[id] = agree }

        setState(state().copy(permissions = map))
        { OptInStateUpdate.Permissions(map) }

    }

    /* --- navigation --- */

    suspend fun back(
        optInNavController: OptInNavController,
        authNavController: AuthNavController,
        rootNavController: RootNavController
    ): Unit {
        if (navigator.back(optInNavController).not())
            if (navigator.back(authNavController).not())
                navigator.back(rootNavController)
    }

    suspend fun permission(optInNavController: OptInNavController): Unit {

        if (state().optIns.permissions.firstOrNull() != null)
            navigator.navigateTo(optInNavController, OptInWelcomeToOptInPermission(0))

    }

    private suspend fun nextPermission(
        optInNavController: OptInNavController,
        currentIndex: Int
    ): Unit {

        if (state().optIns.permissions.size > (currentIndex + 1))
            navigator.navigateTo(
                optInNavController,
                OptInPermissionToOptInPermission(currentIndex + 1)
            )
        else success(optInNavController)

    }

    private suspend fun success(optInNavController: OptInNavController): Unit =
        navigator.navigateTo(optInNavController, OptInPermissionToOptInSuccess)

    suspend fun consentUser(authNavController: AuthNavController): Unit =
        navigator.navigateTo(authNavController, OptInToConsentUser)

    suspend fun toastError(error: ForYouAndMeError): Unit =
        navigator.performAction(toastAction(error))

    private suspend fun alert(title: String, message: String, close: String): Unit =
        navigator.performAction(alertAction(title, message, close))

    suspend fun web(navController: RootNavController, url: String): Unit =
        navigator.navigateTo(navController, AnywhereToWeb(url))

}