package org.fouryouandme.core.data.api.configuration.response

import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.toOption
import com.squareup.moshi.Json
import org.fouryouandme.core.entity.configuration.*

data class StringsResponse(

    @Json(name = "WELCOME_START_BUTTON") val welcomeStartButton: String? = null,

    @Json(name = "INTRO_TITLE") val introTitle: String? = null,
    @Json(name = "INTRO_BODY") val introBody: String? = null,
    @Json(name = "INTRO_LOGIN") val introLogin: String? = null,
    @Json(name = "INTRO_BACK") val introBack: String? = null,

    @Json(name = "SETUP_LATER_BODY") val setupLaterBody: String? = null,
    @Json(name = "SETUP_LATER_CONFIRM_BUTTON") val setupLaterConfirmButton: String? = null,

    @Json(name = "PHONE_VERIFICATION_TITLE") val phoneVerificationTitle: String? = null,
    @Json(name = "PHONE_VERIFICATION_BODY") val phoneVerificationBody: String? = null,
    @Json(name = "PHONE_VERIFICATION_LEGAL") val phoneVerificationLegal: String? = null,
    @Json(name = "PHONE_VERIFICATION_LEGAL_PRIVACY_POLICY") val phoneVerificationLegalPrivacyPolicy: String? = null,
    @Json(name = "PHONE_VERIFICATION_LEGAL_TERMS_OF_SERVICE") val phoneVerificationLegalTermsOfService: String? = null,
    @Json(name = "PHONE_VERIFICATION_NUMBER_DESCRIPTION") val phoneVerificationNumberDescription: String? = null,

    @Json(name = "PHONE_VERIFICATION_CODE_TITLE") val phoneVerificationCodeTitle: String? = null,
    @Json(name = "PHONE_VERIFICATION_CODE_BODY") val phoneVerificationCodeBody: String? = null,
    @Json(name = "PHONE_VERIFICATION_CODE_DESCRIPTION") val phoneVerificationCodeDescription: String? = null,
    @Json(name = "PHONE_VERIFICATION_CODE_RESEND") val phoneVerificationResendCode: String? = null,

    @Json(name = "PHONE_VERIFICATION_WRONG_NUMBER") val phoneVerificationWrongNumber: String? = null,
    @Json(name = "PHONE_VERIFICATION_ERROR_WRONG_CODE") val phoneVerificationErrorWrongCode: String? = null,
    @Json(name = "PHONE_VERIFICATION_ERROR_MISSING_NUMBER") val phoneVerificationErrorMissingNumber: String? = null,

    @Json(name = "ONBOARDING_ABORT_TITLE") val onboardingAbortTitle: String? = null,
    @Json(name = "ONBOARDING_ABORT_BUTTON") val onboardingAbortButton: String? = null,
    @Json(name = "ONBOARDING_ABORT_CANCEL") val onboardingAbortCancel: String? = null,
    @Json(name = "ONBOARDING_ABORT_CONFIRM") val onboardingAbortConfirm: String? = null,
    @Json(name = "ONBOARDING_ABORT_MESSAGE") val onboradingAbortMessage: String? = null,
    @Json(name = "ONBOARDING_AGREE_BUTTON") val onboradingAgreeButton: String? = null,
    @Json(name = "ONBOARDING_DISAGREE_BUTTON") val onboradingDisagreeButton: String? = null,

    @Json(name = "ONBOARDING_USER_NAME_TITLE") val onboardingUserNameTitle: String? = null,
    @Json(name = "ONBOARDING_USER_NAME_LAST_NAME_DESCRIPTION") val onboardingNameLastNameDescription: String? = null,
    @Json(name = "ONBOARDING_USER_NAME_FIRST_NAME_DESCRIPTION") val onboardingNameFirstNameDescription: String? = null,
    @Json(name = "ONBOARDING_USER_SIGNATURE_TITLE") val onboardingSignatureTitle: String? = null,
    @Json(name = "ONBOARDING_USER_SIGNATURE_BODY") val onboardingSignatureBody: String? = null,
    @Json(name = "ONBOARDING_USER_SIGNATURE_CLEAR") val onboardingSignatureClear: String? = null,
    @Json(name = "ONBOARDING_USER_SIGNATURE_PLACEHOLDER") val onboardingSignaturePlaceholder: String? = null,
    @Json(name = "ONBOARDING_USER_EMAIL_INFO") val onboardingEmailInfo: String? = null,
    @Json(name = "ONBOARDING_USER_EMAIL_EMAIL_DESCRIPTION") val onboardingEmailDescription: String? = null,
    @Json(name = "ONBOARDING_USER_EMAIL_VERIFICATION_TITLE") val onboardingEmailVerificationTitle: String? = null,
    @Json(name = "ONBOARDING_USER_EMAIL_VERIFICATION_BODY") val onboardingEmailVerificationBody: String? = null,
    @Json(name = "ONBOARDING_USER_EMAIL_VERIFICATION_CODE_DESCRIPTION") val onboardingEmailVerificationCodeDescription: String? = null,
    @Json(name = "ONBOARDING_USER_EMAIL_VERIFICATION_RESEND") val onboardingEmailVerificationResend: String? = null,
    @Json(name = "ONBOARDING_USER_EMAIL_VERIFICATION_WRONG_EMAIL") val onboardingEmailVerificationWrongMail: String? = null,
    @Json(name = "ONBOARDING_USER_EMAIL_VERIFICATION_ERROR_WRONG_CODE") val onboardingEmailVerificationWrongCode: String? = null,

    @Json(name = "ONBOARDING_OPT_IN_SUBMIT_BUTTON") val onboardingOptInSubmitButton: String? = null,
    @Json(name = "ONBOARDING_OPT_IN_MANDATORY_CLOSE") val onboardingOptInMandatoryClose: String? = null,
    @Json(name = "ONBOARDING_OPT_IN_MANDATORY_TITLE") val onboardingOptInMandatoryTitle: String? = null,
    @Json(name = "ONBOARDING_OPT_IN_MANDATORY_DEFAULT") val onboardingOptInMandatoryDefault: String? = null,

    @Json(name = "ONBOARDING_WEARABLES_DOWNLOAD_BUTTON_DEFAULT") val onboardingWearableDownloadButtonDefault: String? = null,
    @Json(name = "ONBOARDING_WEARABLES_OPEN_APP_BUTTON_DEFAULT") val onboardingWearableOpenAppButtonDefault: String? = null,
    @Json(name = "ONBOARDING_WEARABLES_LOGIN_BUTTON_DEFAULT") val onboardingWearableLoginButtonDefault: String? = null,
    @Json(name = "ONBOARDING_WEARABLES_NEXT_BUTTON_DEFAULT") val onboardingWearableNextButtonDefault: String? = null,

    @Json(name = "TAB_FEED") val tabFeed: String? = null,
    @Json(name = "TAB_TASK") val tabTask: String? = null,
    @Json(name = "TAB_USER_DATA") val tabUserData: String? = null,
    @Json(name = "TAB_STUDY_INFO") val tabStudyInfo: String? = null,

    @Json(name = "URL_PRIVACY_POLICY") val urlPrivacyPolicy: String? = null,
    @Json(name = "URL_TERMS_OF_SERVICE") val urlTermsOfService: String? = null,

    @Json(name = "ERROR_TITLE_DEFAULT") val errorTitleDefault: String? = null,
    @Json(name = "ERROR_MESSAGE_DEFAULT") val errorMessageDefault: String? = null,
    @Json(name = "ERROR_BUTTON_RETRY") val errorButtonRetry: String? = null,
    @Json(name = "ERROR_BUTTON_CANCEL") val errorButtonCancel: String? = null,
    @Json(name = "ERROR_MESSAGE_REMOTE_SERVER") val errorMessageRemoteServer: String? = null,
    @Json(name = "ERROR_MESSAGE_CONNECTIVITY") val errorMessageConnectivity: String? = null

) {

    fun toText(): Option<Text> =
        Option.fx {

            Text(
                Error(
                    errorTitleDefault.toOption().bind(),
                    errorMessageDefault.toOption().bind(),
                    errorButtonRetry.toOption().bind(),
                    errorButtonCancel.toOption().bind(),
                    errorMessageRemoteServer.toOption().bind(),
                    errorMessageConnectivity.toOption().bind()
                ),
                Url(
                    urlPrivacyPolicy.toOption().bind(),
                    urlTermsOfService.toOption().bind()
                ),
                Welcome(welcomeStartButton.toOption().bind()),
                Intro(
                    introTitle.toOption().bind(),
                    introBody.toOption().bind(),
                    introBack.toOption().bind(),
                    introLogin.toOption().bind()
                ),
                SignUpLater(
                    setupLaterBody.toOption().bind(),
                    setupLaterConfirmButton.toOption().bind()
                ),
                PhoneVerification(
                    phoneVerificationTitle.toOption().bind(),
                    phoneVerificationBody.toOption().bind(),
                    phoneVerificationLegal.toOption().bind(),
                    phoneVerificationNumberDescription.toOption().bind(),
                    phoneVerificationLegalPrivacyPolicy.toOption().bind(),
                    phoneVerificationLegalTermsOfService.toOption().bind(),
                    phoneVerificationResendCode.toOption().bind(),
                    phoneVerificationWrongNumber.toOption().bind(),
                    phoneVerificationCodeTitle.toOption().bind(),
                    phoneVerificationCodeBody.toOption().bind(),
                    phoneVerificationCodeDescription.toOption().bind(),
                    PhoneVerificationError(
                        phoneVerificationErrorMissingNumber.toOption().bind(),
                        phoneVerificationErrorWrongCode.toOption().bind()
                    )
                ),
                Onboarding(
                    onboardingAbortTitle.toOption().bind(),
                    onboardingAbortButton.toOption().bind(),
                    onboardingAbortCancel.toOption().bind(),
                    onboardingAbortConfirm.toOption().bind(),
                    onboradingAbortMessage.toOption().bind(),
                    onboradingAgreeButton.toOption().bind(),
                    onboradingDisagreeButton.toOption().bind(),
                    OnboardingUser(
                        onboardingUserNameTitle.toOption().bind(),
                        onboardingNameLastNameDescription.toOption().bind(),
                        onboardingNameFirstNameDescription.toOption().bind(),
                        onboardingSignatureTitle.toOption().bind(),
                        onboardingSignatureBody.toOption().bind(),
                        onboardingSignatureClear.toOption().bind(),
                        onboardingSignaturePlaceholder.toOption().bind(),
                        onboardingEmailInfo.toOption().bind(),
                        onboardingEmailDescription.toOption().bind(),
                        onboardingEmailVerificationTitle.toOption().bind(),
                        onboardingEmailVerificationBody.toOption().bind(),
                        onboardingEmailVerificationCodeDescription.toOption().bind(),
                        onboardingEmailVerificationResend.toOption().bind(),
                        onboardingEmailVerificationWrongMail.toOption().bind(),
                        OnboardingUserError(
                            onboardingEmailVerificationWrongCode.toOption().bind()
                        )
                    ),
                    OnboardingOptIn(
                        onboardingOptInSubmitButton.toOption().bind(),
                        onboardingOptInMandatoryClose.toOption().bind(),
                        onboardingOptInMandatoryTitle.toOption().bind(),
                        onboardingOptInMandatoryDefault.toOption().bind()
                    ),
                    OnboardingWearable(
                        onboardingWearableDownloadButtonDefault.toOption().bind(),
                        onboardingWearableOpenAppButtonDefault.toOption().bind(),
                        onboardingWearableLoginButtonDefault.toOption().bind(),
                        onboardingWearableNextButtonDefault.toOption().bind()
                    )
                ),
                Tab(
                    tabFeed.toOption().bind(),
                    tabTask.toOption().bind(),
                    tabUserData.toOption().bind(),
                    tabStudyInfo.toOption().bind()
                )
            )
        }
}