package org.fouryouandme.auth.optin

import androidx.navigation.NavController
import arrow.Kind
import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.firstOrNone
import arrow.core.getOrElse
import arrow.core.toOption
import arrow.fx.ForIO
import com.karumi.dexter.DexterBuilder
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener
import com.karumi.dexter.listener.single.BasePermissionListener
import kotlinx.coroutines.delay
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.arch.error.handleAuthError
import org.fouryouandme.core.arch.navigation.*
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.configuration.ConfigurationUseCase
import org.fouryouandme.core.cases.optins.OptInsUseCase
import org.fouryouandme.core.ext.foldToKindEither
import org.fouryouandme.core.ext.mapResult
import org.fouryouandme.core.ext.unsafeRunAsync


class OptInViewModel(
    navigator: Navigator,
    runtime: Runtime<ForIO>
) : BaseViewModel<
        ForIO,
        OptInState,
        OptInStateUpdate,
        OptInError,
        OptInLoading>
    (OptInState(), navigator, runtime) {

    /* --- data --- */

    fun initialize(rootNavController: RootNavController): Unit =
        runtime.fx.concurrent {

            !showLoading(OptInLoading.Initialization)

            val configuration =
                !ConfigurationUseCase.getConfiguration(runtime, CachePolicy.MemoryFirst)

            val initialization =
                !configuration.foldToKindEither(runtime.fx) { config ->

                    OptInsUseCase.getOptIns(runtime)
                        .mapResult(runtime.fx) { it to config }

                }.handleAuthError(runtime, rootNavController, navigator)

            !initialization.fold(
                { setError(it, OptInError.Initialization) },
                { pair ->
                    setState(
                        state().copy(
                            optIns = pair.first.toOption(),
                            configuration = pair.second.toOption()
                        ),
                        OptInStateUpdate.Initialization(pair.second, pair.first)
                    )
                }
            )

            !hideLoading(OptInLoading.Initialization)

        }.unsafeRunAsync()

    /* --- permission --- */

    private fun setPermission(
        rootNavController: RootNavController,
        navController: NavController,
        index: Int,
        permissionId: String,
        agree: Boolean
    ): Kind<ForIO, Unit> =
        runtime.fx.concurrent {

            !showLoading(OptInLoading.PermissionSet)

            // TODO: remove this line when the api is ready
            !effect { delay(3000) }

            /*val set =
                !OptInsUseCase.setPermission(runtime, permissionId, agree)
                    .handleAuthError(runtime, rootNavController, navigator)

            !set.fold(
                { setError(it, OptInError.PermissionSet) },
                { nextPermission(navController, index) }
            )*/

            // TODO: uncomment and remove this line when the api is ready
            !nextPermission(navController, index)

            !hideLoading(OptInLoading.PermissionSet)

        }

    fun requestPermissions(
        rootNavController: RootNavController,
        navController: NavController,
        dexter: DexterBuilder.Permission,
        index: Int
    ): Unit {

        Option.fx {

            val agree = !state().permissions[index].toOption()
            val optIn =
                !state().optIns
                    .bind()
                    .permissions
                    .getOrNull(index)
                    .toOption()


            if (agree) {

                when (optIn.systemPermissions.size) {
                    0 ->
                        setPermission(
                            rootNavController,
                            navController,
                            index,
                            optIn.id,
                            agree
                        ).unsafeRunAsync()
                    1 ->
                        dexter.withPermission(optIn.systemPermissions[0])
                            .withListener(
                                permissionListener(
                                    rootNavController,
                                    navController,
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
                                    navController,
                                    index,
                                    optIn.id,
                                    agree
                                )
                            )
                            .check()
                }

            } else {

                if (optIn.required) state().configuration.map {
                    alert(
                        it.text.onboarding.optIn.mandatoryTitle,
                        it.text.onboarding.optIn.mandatoryDefault,
                        it.text.onboarding.optIn.mandatoryClose
                    )
                }
                else nextPermission(navController, index)
            }
        }

    }

    private fun permissionListener(
        rootNavController: RootNavController,
        navController: NavController,
        index: Int,
        permissionId: String,
        agree: Boolean
    ): BasePermissionListener =
        object : BasePermissionListener() {

            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                setPermission(
                    rootNavController,
                    navController,
                    index,
                    permissionId,
                    agree
                ).unsafeRunAsync()
            }

            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                setPermission(
                    rootNavController,
                    navController,
                    index,
                    permissionId,
                    agree
                ).unsafeRunAsync()
            }
        }

    private fun multiplePermissionListener(
        rootNavController: RootNavController,
        navController: NavController,
        index: Int,
        permissionId: String,
        agree: Boolean
    ): BaseMultiplePermissionsListener =
        object : BaseMultiplePermissionsListener() {

            override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                setPermission(
                    rootNavController,
                    navController,
                    index,
                    permissionId,
                    agree
                ).unsafeRunAsync()
            }

        }

    /* --- state --- */

    fun setPermissionState(id: Int, agree: Boolean): Unit {

        val map =
            state().permissions.toMutableMap()
                .also { it[id] = agree }

        setState(
            state().copy(permissions = map),
            OptInStateUpdate.Permissions(map)
        ).unsafeRunAsync()

    }

    /* --- navigation --- */

    fun back(navController: NavController): Unit =
        navigator.back(runtime, navController).unsafeRunAsync()

    fun sectionBack(navController: RootNavController): Unit =
        navigator.back(runtime, navController).unsafeRunAsync()

    fun permission(navController: NavController): Unit =
        runtime.fx.concurrent {

            !state().optIns.flatMap { it.permissions.firstOrNone() }
                .fold(
                    { just(Unit) },
                    {
                        navigator.navigateTo(
                            runtime,
                            navController,
                            OptInWelcomeToOptInPermission(0)
                        )
                    }
                )

        }.unsafeRunAsync()

    private fun nextPermission(
        navController: NavController,
        currentIndex: Int
    ): Kind<ForIO, Unit> =
        runtime.fx.concurrent {

            !if (state().optIns
                    .map {
                        it.permissions.size >
                                (currentIndex + 1)
                    }
                    .getOrElse { false }
            )
                navigator.navigateTo(
                    runtime,
                    navController,
                    OptInPermissionToOptInPermission(currentIndex + 1)
                )
            else success(navController)

        }

    // TODO: success page
    private fun success(navController: NavController): Kind<ForIO, Unit> =
        navigator.navigateTo(
            runtime,
            navController,
            OptInPermissionToOptInSuccess
        )

    fun consentUser(rootNavController: RootNavController): Unit =
        navigator.navigateTo(
            runtime,
            rootNavController,
            OptInToConsentUser
        ).unsafeRunAsync()

    fun toastError(error: FourYouAndMeError): Unit =
        navigator.performAction(runtime, toastAction(error)).unsafeRunAsync()

    private fun alert(title: String, message: String, close: String): Unit =
        navigator.performAction(runtime, alertAction(title, message, close)).unsafeRunAsync()

    fun web(navController: RootNavController, url: String): Unit =
        navigator.navigateTo(runtime, navController, AnywhereToWeb(url)).unsafeRunAsync()

}