package org.fouryouandme.auth.consent.user

import android.graphics.Bitmap
import android.util.Base64
import android.util.Patterns
import androidx.navigation.NavController
import arrow.Kind
import arrow.core.toOption
import arrow.fx.ForIO
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
import org.fouryouandme.core.cases.consent.user.ConsentUserUseCase
import org.fouryouandme.core.ext.foldToKindEither
import org.fouryouandme.core.ext.mapResult
import org.fouryouandme.core.ext.unsafeRunAsync
import java.io.ByteArrayOutputStream


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

    fun initialize(rootNavController: RootNavController): Unit =
        runtime.fx.concurrent {

            !showLoading(ConsentUserLoading.Initialization)

            val configuration =
                !ConfigurationUseCase.getConfiguration(runtime, CachePolicy.MemoryFirst)

            val initialization =
                !configuration.foldToKindEither(runtime.fx) { config ->

                    ConsentUserUseCase.getConsent(runtime)
                        .mapResult(runtime.fx) { it to config }

                }.handleAuthError(runtime, rootNavController, navigator)

            !initialization.fold(
                { setError(it, ConsentUserError.Initialization) },
                { pair ->
                    setState(
                        state().copy(
                            consent = pair.first.toOption(),
                            configuration = pair.second.toOption()
                        ),
                        ConsentUserStateUpdate.Initialization(pair.second)
                    )
                }
            )

            !hideLoading(ConsentUserLoading.Initialization)

        }.unsafeRunAsync()

    /* --- user --- */

    fun createUser(rootNavController: RootNavController, navController: NavController): Unit =
        runtime.fx.concurrent {

            !showLoading(ConsentUserLoading.CreateUser)

            val create =
                !ConsentUserUseCase.createUserConsent(runtime, state().email)
                    .handleAuthError(
                        runtime,
                        rootNavController,
                        navigator
                    )

            !create.fold(
                { setError(it, ConsentUserError.CreateUser) },
                { emailVerification(navController) }
            )

            !hideLoading(ConsentUserLoading.CreateUser)

        }.unsafeRunAsync()

    fun resendEmail(rootNavController: RootNavController): Unit =
        runtime.fx.concurrent {

            !showLoading(ConsentUserLoading.ResendConfirmationEmail)

            val resend =
                !ConsentUserUseCase.resendConfirmationEmail(runtime)
                    .handleAuthError(runtime, rootNavController, navigator)

            !resend.fold(
                { setError(it, ConsentUserError.ResendConfirmationEmail) },
                { navigator.performAction(runtime, toastAction("Email sent successfully")) }
            )

            !hideLoading(ConsentUserLoading.ResendConfirmationEmail)

        }.unsafeRunAsync()

    fun confirmEmail(
        rootNavController: RootNavController,
        navController: NavController,
        code: String
    ): Unit =
        runtime.fx.concurrent {

            !showLoading(ConsentUserLoading.ConfirmEmail)

            val resend =
                !ConsentUserUseCase.confirmEmail(runtime, code)
                    .handleAuthError(runtime, rootNavController, navigator)

            !resend.fold(
                { setError(it, ConsentUserError.ConfirmEmail) },
                { signature(navController) }
            )

            !hideLoading(ConsentUserLoading.ConfirmEmail)

        }.unsafeRunAsync()

    fun updateUser(
        rootNavController: RootNavController,
        navController: NavController,
        signature: Bitmap
    ): Unit =
        runtime.fx.concurrent {

            !showLoading(ConsentUserLoading.UpdateUser)

            val signatureBase64 = signature.toBase64()

            val update =
                !ConsentUserUseCase.updateUserConsent(
                    runtime,
                    state().firstName,
                    state().lastName,
                    signatureBase64
                ).handleAuthError(runtime, rootNavController, navigator)

            !update.fold(
                { setError(it, ConsentUserError.UpdateUser) },
                { success(navController) }
            )

            !hideLoading(ConsentUserLoading.UpdateUser)

        }.unsafeRunAsync()

    private fun Bitmap.toBase64(): String {

        val byteArrayOutputStream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)

    }

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

    private fun signature(navController: NavController): Kind<ForIO, Unit> =
        navigator.navigateTo(
            runtime,
            navController,
            ConsentUserEmailValidationCodeToConsentUserSignature
        )

    private fun success(navController: NavController): Kind<ForIO, Unit> =
        navigator.navigateTo(
            runtime,
            navController,
            ConsentUserSignatureToConsentUserSuccess
        )

    fun toastError(error: FourYouAndMeError): Unit =
        navigator.performAction(runtime, toastAction(error)).unsafeRunAsync()

    fun web(navController: RootNavController, url: String): Unit =
        navigator.navigateTo(runtime, navController, AnywhereToWeb(url)).unsafeRunAsync()

    fun wearable(rootNavController: RootNavController): Unit =
        navigator.navigateTo(runtime, rootNavController, ConsentUserToWearable).unsafeRunAsync()

}