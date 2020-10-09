package org.fouryouandme.auth.optin

import arrow.core.Either
import arrow.core.getOrElse
import arrow.core.left
import arrow.core.right
import arrow.fx.ForIO
import com.karumi.dexter.DexterBuilder
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener
import com.karumi.dexter.listener.single.BasePermissionListener
import org.fouryouandme.auth.AuthNavController
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.deps.modules.OptInModule
import org.fouryouandme.core.arch.deps.modules.nullToError
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.arch.error.handleAuthError
import org.fouryouandme.core.arch.navigation.*
import org.fouryouandme.core.cases.optins.OptInsUseCase.getOptIns
import org.fouryouandme.core.cases.optins.OptInsUseCase.setPermission
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.ext.mapNotNull
import org.fouryouandme.core.ext.startCoroutineAsync


class OptInViewModel(
    navigator: Navigator,
    runtime: Runtime<ForIO>,
    private val optInModule: OptInModule
) : BaseViewModel<
        ForIO,
        OptInState,
        OptInStateUpdate,
        OptInError,
        OptInLoading>
    (navigator = navigator, runtime = runtime) {

    /* --- data --- */

    suspend fun initialize(
        rootNavController: RootNavController
    ): Either<FourYouAndMeError, OptInState> {

        showLoadingFx(OptInLoading.Initialization)

        val state =
            optInModule.getOptIns()
                .nullToError()
                .handleAuthError(rootNavController, navigator)
                .fold(
                    {
                        setErrorFx(it, OptInError.Initialization)
                        it.left()
                    },
                    { optIns ->

                        val state = OptInState(optIns, emptyMap())

                        setStateFx(state) { OptInStateUpdate.Initialization(it.optIns) }

                        state.right()
                    }
                )

        hideLoadingFx(OptInLoading.Initialization)

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

        showLoadingFx(OptInLoading.PermissionSet)

        optInModule.setPermission(permissionId, agree)
            .handleAuthError(rootNavController, navigator).fold(
                { setErrorFx(it, OptInError.PermissionSet) },
                { nextPermission(optInNavController, index) }
            )

        hideLoadingFx(OptInLoading.PermissionSet)

    }

    suspend fun requestPermissions(
        configuration: Configuration,
        rootNavController: RootNavController,
        optInNavController: OptInNavController,
        dexter: DexterBuilder.Permission,
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
                        1 ->
                            dexter.withPermission(optIn.systemPermissions[0])
                                .withListener(
                                    permissionListener(
                                        rootNavController,
                                        optInNavController,
                                        index,
                                        optIn.id,
                                        agree
                                    )
                                )
                                .check()
                        else ->
                            dexter.withPermissions(optIn.systemPermissions)
                                .withListener(
                                    multiplePermissionListener(
                                        rootNavController,
                                        optInNavController,
                                        index,
                                        optIn.id,
                                        agree
                                    )
                                )
                                .check()
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

    private fun permissionListener(
        rootNavController: RootNavController,
        optInNavController: OptInNavController,
        index: Int,
        permissionId: String,
        agree: Boolean
    ): BasePermissionListener =
        object : BasePermissionListener() {

            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                startCoroutineAsync {
                    setPermission(
                        rootNavController,
                        optInNavController,
                        index,
                        permissionId,
                        agree
                    )
                }
            }

            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                startCoroutineAsync {
                    setPermission(
                        rootNavController,
                        optInNavController,
                        index,
                        permissionId,
                        agree
                    )
                }
            }
        }

    private fun multiplePermissionListener(
        rootNavController: RootNavController,
        optInNavController: OptInNavController,
        index: Int,
        permissionId: String,
        agree: Boolean
    ): BaseMultiplePermissionsListener =
        object : BaseMultiplePermissionsListener() {

            override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                startCoroutineAsync {
                    setPermission(
                        rootNavController,
                        optInNavController,
                        index,
                        permissionId,
                        agree
                    )
                }
            }

        }

    suspend fun setPermissionState(id: Int, agree: Boolean): Unit {

        val map =
            state().permissions.toMutableMap().also { it[id] = agree }

        setStateFx(state().copy(permissions = map))
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

    suspend fun toastError(error: FourYouAndMeError): Unit =
        navigator.performAction(toastAction(error))

    private suspend fun alert(title: String, message: String, close: String): Unit =
        navigator.performAction(alertAction(title, message, close))

    suspend fun web(navController: RootNavController, url: String): Unit =
        navigator.navigateTo(navController, AnywhereToWeb(url))

}