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
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.arch.error.handleAuthError
import org.fouryouandme.core.arch.navigation.AnywhereToWeb
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.arch.navigation.RootNavController
import org.fouryouandme.core.arch.navigation.toastAction
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

    fun requestPermissions(
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


            // TODO: check required
            if (agree) {

                when (optIn.systemPermissions.size) {
                    0 -> nextPermission(navController, index)
                    1 ->
                        dexter.withPermission(optIn.systemPermissions[0])
                            .withListener(permissionListener(navController, index))
                            .check()
                    else ->
                        dexter.withPermissions(optIn.systemPermissions)
                            .withListener(multiplePermissionListener(navController, index))
                            .check()
                }

            } else nextPermission(navController, index)
        }

    }

    private fun permissionListener(
        navController: NavController,
        index: Int
    ): BasePermissionListener =
        object : BasePermissionListener() {

            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                nextPermission(navController, index)
            }

            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                nextPermission(navController, index)
            }
        }

    private fun multiplePermissionListener(
        navController: NavController,
        index: Int
    ): BaseMultiplePermissionsListener =
        object : BaseMultiplePermissionsListener() {

            override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                nextPermission(navController, index)
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

    private fun nextPermission(navController: NavController, currentIndex: Int): Unit =
        runtime.fx.concurrent {

            if (state().optIns
                    .map {
                        it.permissions.size >
                                (currentIndex + 1)
                    }
                    .getOrElse { false }
            )

                !navigator.navigateTo(
                    runtime,
                    navController,
                    OptInPermissionToOptInPermission(currentIndex + 1)
                )
            else success(navController)

        }.unsafeRunAsync()

    // TODO: success page
    private fun success(navController: NavController): Kind<ForIO, Unit> =
        runtime.fx.concurrent { }

    fun toastError(error: FourYouAndMeError): Unit =
        navigator.performAction(runtime, toastAction(error)).unsafeRunAsync()

    fun web(navController: RootNavController, url: String): Unit =
        navigator.navigateTo(runtime, navController, AnywhereToWeb(url)).unsafeRunAsync()

}