package org.fouryouandme.auth.consent.user

import android.util.Patterns
import androidx.navigation.NavController
import arrow.Kind
import arrow.core.right
import arrow.core.toOption
import arrow.fx.ForIO
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.arch.error.handleAuthError
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.arch.navigation.toastAction
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.configuration.ConfigurationUseCase
import org.fouryouandme.core.cases.consent.user.ConsentUserUseCase
import org.fouryouandme.core.ext.foldToKindEither
import org.fouryouandme.core.ext.unsafeRunAsync

class ConsentUserViewModel(
    navigator: Navigator,
    runtime: Runtime<ForIO>
) : BaseViewModel<
        ForIO,
        ConsentUserState,
        ConsentUserStateUpdate,
        ConsentUserError,
        ConsentUserLoading>
    (ConsentUserState(), navigator, runtime) {

    /* --- data --- */

    fun initialize(navController: NavController): Unit =
        runtime.fx.concurrent {

            !showLoading(ConsentUserLoading.Initialization)

            val configuration =
                !ConfigurationUseCase.getConfiguration(runtime, CachePolicy.MemoryFirst)

            val initialization =
                !configuration.foldToKindEither(runtime.fx) { config ->

                    /*ConsentUserUseCase.getConsent(runtime)
                        .mapResult(runtime.fx) { it to config }*/

                    just((Unit to config).right())

                }.handleAuthError(runtime, navController, navigator)

            !initialization.fold(
                { setError(it, ConsentUserError.Initialization) },
                { pair ->
                    setState(
                        state().copy(
                            configuration = pair.second.toOption()
                        ),
                        ConsentUserStateUpdate.Initialization(pair.second)
                    )
                }
            )

            !hideLoading(ConsentUserLoading.Initialization)

        }.unsafeRunAsync()

    /* --- user --- */

    fun createUser(navController: NavController): Unit =
        runtime.fx.concurrent {

            !showLoading(ConsentUserLoading.CreateUser)

            val create =
                !ConsentUserUseCase.createUserConsent(runtime, state().email)

            !create.fold(
                { setError(it, ConsentUserError.CreateUser) },
                { emailVerification(navController) }
            )

            !hideLoading(ConsentUserLoading.CreateUser)

        }.unsafeRunAsync()

    /* --- validation --- */

    fun isValidEmail(email: String): Boolean =
        email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()

    /* --- state --- */

    fun setFirstName(firstName: String): Unit =
        setState(
            state().copy(firstName = firstName),
            ConsentUserStateUpdate.FirstName(firstName)
        ).unsafeRunAsync()

    fun setLastName(lastName: String): Unit =
        setState(
            state().copy(lastName = lastName),
            ConsentUserStateUpdate.FirstName(lastName)
        ).unsafeRunAsync()

    fun setEmail(email: String): Unit =
        setState(
            state().copy(email = email),
            ConsentUserStateUpdate.Email(email)
        ).unsafeRunAsync()

    /* --- navigation --- */

    fun back(navController: NavController): Unit =
        navigator.back(runtime, navController).unsafeRunAsync()

    fun email(navController: NavController): Unit =
        navigator.navigateTo(
            runtime,
            navController,
            ConsentUserNameToConsentUserEmail
        ).unsafeRunAsync()

    private fun emailVerification(navController: NavController): Kind<ForIO, Unit> =
        navigator.navigateTo(
            runtime,
            navController,
            ConsentUserEmailToConsentUserEmailValidationCode
        )

    fun signature(navController: NavController): Unit =
        navigator.navigateTo(
            runtime,
            navController,
            ConsentUserEmailValidationCodeToConsentUserSignature
        ).unsafeRunAsync()

    fun toastError(error: FourYouAndMeError): Unit =
        navigator.performAction(runtime, toastAction(error)).unsafeRunAsync()

}