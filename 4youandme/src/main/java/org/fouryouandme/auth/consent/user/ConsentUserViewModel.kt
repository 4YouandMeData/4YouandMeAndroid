package org.fouryouandme.auth.consent.user

import android.graphics.Bitmap
import android.util.Base64
import android.util.Patterns
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import org.fouryouandme.auth.AuthNavController
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.modules.ConsentUserModule
import org.fouryouandme.core.arch.deps.modules.nullToError
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.arch.error.handleAuthError
import org.fouryouandme.core.arch.navigation.AnywhereToWeb
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.arch.navigation.RootNavController
import org.fouryouandme.core.arch.navigation.toastAction
import org.fouryouandme.core.cases.consent.user.ConsentUserUseCase.confirmEmail
import org.fouryouandme.core.cases.consent.user.ConsentUserUseCase.createUserConsent
import org.fouryouandme.core.cases.consent.user.ConsentUserUseCase.getConsent
import org.fouryouandme.core.cases.consent.user.ConsentUserUseCase.resendConfirmationEmail
import org.fouryouandme.core.cases.consent.user.ConsentUserUseCase.updateUserConsent
import java.io.ByteArrayOutputStream


class ConsentUserViewModel(
    navigator: Navigator,
    private val consentUserModule: ConsentUserModule
) : BaseViewModel<
        ConsentUserState,
        ConsentUserStateUpdate,
        ConsentUserError,
        ConsentUserLoading>
    (navigator = navigator) {

    /* --- initialization --- */

    suspend fun initialize(rootNavController: RootNavController): Either<FourYouAndMeError, ConsentUserState> {

        showLoading(ConsentUserLoading.Initialization)


        val state =
            consentUserModule.getConsent()
                .nullToError()
                .handleAuthError(rootNavController, navigator)
                .fold(
                    {
                        setError(it, ConsentUserError.Initialization)
                        it.left()
                    },
                    {

                        val state =
                            ConsentUserState(consent = it)

                        setState(state)
                        { ConsentUserStateUpdate.Initialization(state.consent) }

                        state.right()

                    }
                )

        hideLoading(ConsentUserLoading.Initialization)

        return state

    }

    /* --- user --- */

    suspend fun createUser(
        rootNavController: RootNavController,
        consentUserNavController: ConsentUserNavController
    ): Unit {

        showLoading(ConsentUserLoading.CreateUser)

        consentUserModule.createUserConsent(state().email)
            .handleAuthError(rootNavController, navigator).fold(
                { setError(it, ConsentUserError.CreateUser) },
                { emailVerification(consentUserNavController) }
            )

        hideLoading(ConsentUserLoading.CreateUser)

    }

    suspend fun resendEmail(rootNavController: RootNavController): Unit {

        showLoading(ConsentUserLoading.ResendConfirmationEmail)

        consentUserModule.resendConfirmationEmail()
            .handleAuthError(rootNavController, navigator)
            .fold(
                { setError(it, ConsentUserError.ResendConfirmationEmail) },
                // TODO: remove hardcoded string
                { navigator.performAction(toastAction("Email sent successfully")) }
            )

        hideLoading(ConsentUserLoading.ResendConfirmationEmail)

    }

    suspend fun confirmEmail(
        rootNavController: RootNavController,
        consentUserNavController: ConsentUserNavController,
        code: String
    ): Unit {

        showLoading(ConsentUserLoading.ConfirmEmail)


        consentUserModule.confirmEmail(code)
            .handleAuthError(rootNavController, navigator)
            .fold(
                { setError(it, ConsentUserError.ConfirmEmail) },
                { signature(consentUserNavController) }
            )

        hideLoading(ConsentUserLoading.ConfirmEmail)

    }

    suspend fun updateUser(
        rootNavController: RootNavController,
        consentUserNavController: ConsentUserNavController,
        signature: Bitmap
    ): Unit {

        showLoading(ConsentUserLoading.UpdateUser)

        val signatureBase64 = signature.toBase64()

        consentUserModule.updateUserConsent(
            state().firstName,
            state().lastName,
            signatureBase64
        )
            .handleAuthError(rootNavController, navigator)
            .fold(
                { setError(it, ConsentUserError.UpdateUser) },
                { success(consentUserNavController) }
            )

        hideLoading(ConsentUserLoading.UpdateUser)

    }

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

    suspend fun setFirstName(firstName: String): Unit =
        setState(state().copy(firstName = firstName))
        { ConsentUserStateUpdate.FirstName(firstName) }

    suspend fun setLastName(lastName: String): Unit =
        setState(state().copy(lastName = lastName))
        { ConsentUserStateUpdate.FirstName(lastName) }

    suspend fun setEmail(email: String): Unit =
        setState(state().copy(email = email))
        { ConsentUserStateUpdate.Email(email) }

    /* --- navigation --- */

    suspend fun back(
        consentUserNavController: ConsentUserNavController,
        authNavController: AuthNavController,
        rootNavController: RootNavController
    ): Unit {
        if (navigator.back(consentUserNavController).not())
            if (navigator.back(authNavController).not())
                navigator.back(rootNavController)
    }

    suspend fun email(consentUserNavController: ConsentUserNavController): Unit =
        navigator.navigateTo(
            consentUserNavController,
            ConsentUserNameToConsentUserEmail
        )

    private suspend fun emailVerification(consentUserNavController: ConsentUserNavController): Unit =
        navigator.navigateTo(
            consentUserNavController,
            ConsentUserEmailToConsentUserEmailValidationCode
        )

    private suspend fun signature(consentUserNavController: ConsentUserNavController): Unit =
        navigator.navigateTo(
            consentUserNavController,
            ConsentUserEmailValidationCodeToConsentUserSignature
        )

    private suspend fun success(consentUserNavController: ConsentUserNavController): Unit =
        navigator.navigateTo(
            consentUserNavController,
            ConsentUserSignatureToConsentUserSuccess
        )

    suspend fun toastError(error: FourYouAndMeError): Unit =
        navigator.performAction(toastAction(error))

    suspend fun web(navController: RootNavController, url: String): Unit =
        navigator.navigateTo(navController, AnywhereToWeb(url))

    suspend fun integration(authNavController: AuthNavController): Unit =
        navigator.navigateTo(authNavController, ConsentUserToIntegration)

}