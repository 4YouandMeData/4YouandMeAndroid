package com.foryouandme.ui.auth.onboarding.step.consent.optin

import arrow.core.Either
import arrow.core.getOrElse
import arrow.core.left
import arrow.core.right
import com.foryouandme.ui.auth.AuthNavController
import com.foryouandme.ui.auth.onboarding.step.OnboardingStepNavController
import com.foryouandme.ui.auth.onboarding.step.consent.ConsentNavController
import com.foryouandme.core.arch.android.BaseViewModel
import com.foryouandme.core.arch.deps.modules.OptInModule
import com.foryouandme.core.arch.deps.modules.PermissionModule
import com.foryouandme.core.arch.deps.modules.nullToError
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.arch.error.handleAuthError
import com.foryouandme.core.arch.navigation.*
import com.foryouandme.core.arch.navigation.action.alertAction
import com.foryouandme.core.arch.navigation.action.toastAction
import com.foryouandme.core.cases.optins.OptInsUseCase.getOptIns
import com.foryouandme.core.cases.optins.OptInsUseCase.setPermission
import com.foryouandme.core.cases.permission.PermissionUseCase.requestPermission
import com.foryouandme.core.cases.permission.PermissionUseCase.requestPermissions
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.core.ext.mapNotNull


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
        consentNavController: ConsentNavController,
        onboardingStepNavController: OnboardingStepNavController,
        authNavController: AuthNavController,
        rootNavController: RootNavController
    ): Boolean =
        if (navigator.backSuspend(optInNavController).not())
            if (navigator.backSuspend(consentNavController).not())
                if (navigator.backSuspend(onboardingStepNavController).not())
                    if (navigator.backSuspend(authNavController).not())
                        navigator.backSuspend(rootNavController)
                    else true
                else true
            else true
        else true

    suspend fun permission(optInNavController: OptInNavController): Unit {

        if (state().optIns.permissions.firstOrNull() != null)
            navigator.navigateToSuspend(optInNavController, OptInWelcomeToOptInPermission(0))

    }

    private suspend fun nextPermission(
        optInNavController: OptInNavController,
        currentIndex: Int
    ): Unit {

        if (state().optIns.permissions.size > (currentIndex + 1))
            navigator.navigateToSuspend(
                optInNavController,
                OptInPermissionToOptInPermission(currentIndex + 1)
            )
        else success(optInNavController)

    }

    private suspend fun success(optInNavController: OptInNavController): Unit =
        navigator.navigateToSuspend(optInNavController, OptInPermissionToOptInSuccess)

    suspend fun consentUser(consentNavController: ConsentNavController): Unit =
        navigator.navigateToSuspend(consentNavController, OptInToConsentUser)

    suspend fun toastError(error: ForYouAndMeError): Unit =
        navigator.performActionSuspend(toastAction(error))

    private suspend fun alert(title: String, message: String, close: String): Unit =
        navigator.performActionSuspend(alertAction(title, message, close))

    suspend fun web(navController: RootNavController, url: String): Unit =
        navigator.navigateToSuspend(navController, AnywhereToWeb(url))

}