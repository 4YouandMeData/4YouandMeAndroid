package org.fouryouandme.core.data.api.configuration.response

import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.toOption
import com.squareup.moshi.Json
import org.fouryouandme.core.entity.configuration.Text

data class StringsResponse(
    @Json(name = "INTRO_BODY") val introBody: String? = null,
    @Json(name = "SETUP_ERROR_TITLE") val setupErrorTitle: String? = null,
    @Json(name = "ERROR_TITLE_DEFAULT") val errorTitleDefault: String? = null,
    @Json(name = "ERROR_MESSAGE_DEFAULT") val errorMessageDefault: String? = null,
    @Json(name = "INTRO_BACK") val introBack: String? = null,
    @Json(name = "GENERIC_INFO_TITLE") val genericInfoTitle: String? = null,
    @Json(name = "INTRO_TITLE") val introTitle: String? = null,
    @Json(name = "ERROR_BUTTON_RETRY") val errorButtonRetry: String? = null,
    @Json(name = "ERROR_BUTTON_CANCEL") val errorButtonCancel: String? = null,
    @Json(name = "WELCOME_START_BUTTON") val welcomeStartButton: String? = null,
    @Json(name = "ERROR_MESSAGE_REMOTE_SERVER") val errorMessageRemoteButton: String? = null,
    @Json(name = "INTRO_LOGIN") val introLogin: String? = null,
    @Json(name = "ERROR_MESSAGE_CONNECTIVITY") val errorMessageConnectivity: String? = null,
    @Json(name = "SETUP_LATER_BODY") val setupLaterBody: String? = null,
    @Json(name = "SETUP_LATER_CONFIRM_BUTTON") val setupLaterConfirmButton: String? = null,
    @Json(name = "PHONE_VERIFICATION_TITLE") val phoneVerificationTitle: String? = null,
    @Json(name = "PHONE_VERIFICATION_BODY") val phoneVerificationBody: String? = null,
    @Json(name = "PHONE_VERIFICATION_LEGAL") val phoneVerificationLegal: String? = null,
    @Json(name = "PHONE_VERIFICATION_RESEND_CODE") val phoneVerificationResendCode: String? = null,
    @Json(name = "PHONE_VERIFICATION_WRONG_NUMBER") val phoneVerificationWrongNumber: String? = null,
    @Json(name = "PHONE_VERIFICATION_CODE_DESCRIPTION") val phoneVerificationCodeDescription: String? = null,
    @Json(name = "PHONE_VERIFICATION_ERROR_WRONG_CODE") val phoneVerificationErrorWrongCode: String? = null,
    @Json(name = "PHONE_VERIFICATION_NUMBER_DESCRIPTION") val phoneVerificationNumberDescription: String? = null,
    @Json(name = "PHONE_VERIFICATION_ERROR_MISSING_NUMBER") val phoneVerificationErrorMissingNumber: String? = null,
    @Json(name = "PHONE_VERIFICATION_LEGAL_PRIVACY_POLICY") val phoneVerificationLegalPrivacyPolicy: String? = null,
    @Json(name = "PHONE_VERIFICATION_LEGAL_TERMS_OF_SERVICE") val phoneVerificationLegalTermsOfService: String? = null
) {

    fun toText(): Option<Text> =
        Option.fx {

            Text(
                !errorTitleDefault.toOption(),
                !errorMessageDefault.toOption(),
                !errorButtonRetry.toOption(),
                !errorButtonCancel.toOption(),
                !errorMessageRemoteButton.toOption(),
                !errorMessageConnectivity.toOption(),
                !setupErrorTitle.toOption(),
                !introTitle.toOption(),
                !introBody.toOption(),
                !introBack.toOption(),
                !introLogin.toOption(),
                !genericInfoTitle.toOption(),
                !welcomeStartButton.toOption(),
                !setupLaterBody.toOption(),
                !setupLaterConfirmButton.toOption(),
                !phoneVerificationTitle.toOption(),
                !phoneVerificationBody.toOption(),
                !phoneVerificationLegal.toOption(),
                !phoneVerificationResendCode.toOption(),
                !phoneVerificationWrongNumber.toOption(),
                !phoneVerificationCodeDescription.toOption(),
                !phoneVerificationErrorWrongCode.toOption(),
                !phoneVerificationNumberDescription.toOption(),
                !phoneVerificationErrorMissingNumber.toOption(),
                !phoneVerificationLegalPrivacyPolicy.toOption(),
                !phoneVerificationLegalTermsOfService.toOption()
            )
        }
}