package com.foryouandme.ui.auth.onboarding.step.consent.user

import android.graphics.Bitmap
import android.util.Base64
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.arch.flow.ErrorFlow
import com.foryouandme.core.arch.flow.LoadingFlow
import com.foryouandme.core.arch.flow.NavigationFlow
import com.foryouandme.core.arch.flow.StateUpdateFlow
import com.foryouandme.core.arch.navigation.AnywhereToWeb
import com.foryouandme.core.arch.navigation.Navigator
import com.foryouandme.core.arch.navigation.RootNavController
import com.foryouandme.core.arch.navigation.action.toastAction
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.domain.usecase.analytics.AnalyticsEvent
import com.foryouandme.domain.usecase.analytics.EAnalyticsProvider
import com.foryouandme.domain.usecase.analytics.SendAnalyticsEventUseCase
import com.foryouandme.domain.usecase.auth.consent.*
import com.foryouandme.ui.auth.AuthNavController
import com.foryouandme.ui.auth.onboarding.step.OnboardingStepNavController
import com.foryouandme.ui.auth.onboarding.step.consent.ConsentNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class ConsentUserViewModel @Inject constructor(
    private val stateUpdateFlow: StateUpdateFlow<ConsentUserStateUpdate>,
    private val loadingFlow: LoadingFlow<ConsentUserLoading>,
    private val errorFlow: ErrorFlow<ConsentUserError>,
    private val navigationFlow: NavigationFlow,
    private val navigator: Navigator,
    private val getConsentUserUseCase: GetConsentUserUseCase,
    private val createConsentUserUseCase: CreateConsentUserUseCase,
    private val updateConsentUserUseCase: UpdateConsentUserUseCase,
    private val resendConfirmationEmailUseCase: ResendConfirmationEmailUseCase,
    private val confirmEmailUseCase: ConfirmEmailUseCase,
    private val sendAnalyticsEventUseCase: SendAnalyticsEventUseCase
) : ViewModel() {

    /* --- state --- */

    var state = ConsentUserState()
        private set

    /* --- flow --- */

    val stateUpdate = stateUpdateFlow.stateUpdates
    val loading = loadingFlow.loading
    val error = errorFlow.error
    val navigation = navigationFlow.navigation

    /* --- consent user --- */

    private suspend fun getConsentUser() {

        loadingFlow.show(ConsentUserLoading.GetConsentUser)

        val consentUser = getConsentUserUseCase()!!
        state = state.copy(consent = consentUser)
        stateUpdateFlow.update(ConsentUserStateUpdate.GetConsentUser)

        loadingFlow.hide(ConsentUserLoading.GetConsentUser)

    }

    /* --- user --- */

    private suspend fun createUser() {

        loadingFlow.show(ConsentUserLoading.CreateUser)

        createConsentUserUseCase(state.email)
        navigationFlow.navigateTo(ConsentUserEmailToConsentUserEmailValidationCode)

        loadingFlow.hide(ConsentUserLoading.CreateUser)

    }

    private suspend fun updateUser(signature: Bitmap) {

        loadingFlow.show(ConsentUserLoading.UpdateUser)

        val signatureBase64 = signature.toBase64()

        updateConsentUserUseCase(state.firstName, state.lastName, signatureBase64)
        navigationFlow.navigateTo(ConsentUserSignatureToConsentUserSuccess)

        loadingFlow.hide(ConsentUserLoading.UpdateUser)

    }

    private fun Bitmap.toBase64(): String {

        val byteArrayOutputStream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)

    }

    /* --- email --- */

    private suspend fun resendEmail() {

        loadingFlow.show(ConsentUserLoading.ResendConfirmationEmail)

        resendConfirmationEmailUseCase()
        // TODO: remove hardcoded string
        navigator.performAction(toastAction("Email sent successfully"))

        loadingFlow.hide(ConsentUserLoading.ResendConfirmationEmail)

    }

    private suspend fun confirmEmail(code: String) {

        loadingFlow.show(ConsentUserLoading.ConfirmEmail)

        confirmEmailUseCase(code)
        navigationFlow.navigateTo(ConsentUserEmailValidationCodeToConsentUserSignature)

        loadingFlow.hide(ConsentUserLoading.ConfirmEmail)

    }

    private suspend fun setEmail(email: String) {
        state = state.copy(email = email, isValidEmail = isValidEmail(email))
        stateUpdateFlow.update(ConsentUserStateUpdate.Email)
    }

    private fun isValidEmail(email: String): Boolean =
        email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()

    /* --- name --- */

    private suspend fun setFirstName(firstName: String) {
        state = state.copy(firstName = firstName)
        stateUpdateFlow.update(ConsentUserStateUpdate.FirstName)
    }

    private suspend fun setLastName(lastName: String) {
        state = state.copy(lastName = lastName)
        stateUpdateFlow.update(ConsentUserStateUpdate.LastName)
    }

    /* --- analytics --- */

    private suspend fun logConsentUserNameScreenViewed() {
        sendAnalyticsEventUseCase(
            AnalyticsEvent.ScreenViewed.ConsentName,
            EAnalyticsProvider.ALL
        )
    }

    private suspend fun logConsentSignatureScreenViewed() {
        sendAnalyticsEventUseCase(
            AnalyticsEvent.ScreenViewed.ConsentSignature,
            EAnalyticsProvider.ALL
        )
    }

    private suspend fun logConsentEmailScreenViewed() {
        sendAnalyticsEventUseCase(
            AnalyticsEvent.ScreenViewed.EmailInsert,
            EAnalyticsProvider.ALL
        )
    }

    private suspend fun logConsentEmailValidationScreenViewed(): Unit =
        sendAnalyticsEventUseCase(
            AnalyticsEvent.ScreenViewed.EmailVerification,
            EAnalyticsProvider.ALL
        )

    fun execute(action: ConsentUserAction) {
        when(action) {
            ConsentUserAction.GetConsentUser ->
                errorFlow.launchCatch(
                    viewModelScope,
                    ConsentUserError.GetConsentUser,
                    loadingFlow,
                    ConsentUserLoading.GetConsentUser
                ) { getConsentUser() }
            ConsentUserAction.CreateUser ->
                viewModelScope.launchSafe { createUser() }
            is ConsentUserAction.SetEmail ->
                viewModelScope.launchSafe { setEmail(action.email) }
            is ConsentUserAction.ConfirmEmail ->
                errorFlow.launchCatch(
                    viewModelScope,
                    ConsentUserError.ConfirmEmail,
                    loadingFlow,
                    ConsentUserLoading.ConfirmEmail
                ) { confirmEmail(action.code)}
            ConsentUserAction.ResendEmail ->
                errorFlow.launchCatch(
                    viewModelScope,
                    ConsentUserError.ResendConfirmationEmail,
                    loadingFlow,
                    ConsentUserLoading.ResendConfirmationEmail
                ) { resendEmail() }
            is ConsentUserAction.SetFirstName ->
                viewModelScope.launchSafe { setFirstName(action.firstName) }
            is ConsentUserAction.SetLastName ->
                viewModelScope.launchSafe { setLastName(action.lastName) }
            is ConsentUserAction.UpdateUser ->
                errorFlow.launchCatch(
                    viewModelScope,
                    ConsentUserError.UpdateUser,
                    loadingFlow,
                    ConsentUserLoading.UpdateUser
                ) { updateUser(action.signature) }
            ConsentUserAction.ConsentEmailViewed ->
                viewModelScope.launchSafe { logConsentEmailScreenViewed() }
            ConsentUserAction.ConsentUserNameViewed ->
                viewModelScope.launchSafe { logConsentUserNameScreenViewed() }
            ConsentUserAction.ConsentUserSignatureViewed ->
                viewModelScope.launchSafe { logConsentSignatureScreenViewed() }
            ConsentUserAction.ConsentEmailValidationViewed ->
                viewModelScope.launchSafe { logConsentEmailValidationScreenViewed() }
        }
    }

}