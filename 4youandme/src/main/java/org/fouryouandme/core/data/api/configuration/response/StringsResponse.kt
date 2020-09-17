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

    @Json(name = "ONBOARDING_WEARABLES_DOWNLOAD_BUTTON_DEFAULT") val onboardingIntegrationDownloadButtonDefault: String? = null,
    @Json(name = "ONBOARDING_WEARABLES_OPEN_APP_BUTTON_DEFAULT") val onboardingIntegrationOpenAppButtonDefault: String? = null,
    @Json(name = "ONBOARDING_WEARABLES_LOGIN_BUTTON_DEFAULT") val onboardingIntegrationLoginButtonDefault: String? = null,
    @Json(name = "ONBOARDING_WEARABLES_NEXT_BUTTON_DEFAULT") val onboardingIntegrationNextButtonDefault: String? = null,

    @Json(name = "TAB_FEED") val tabFeed: String? = null,
    @Json(name = "TAB_TASK") val tabTask: String? = null,
    @Json(name = "TAB_TASK_TITLE") val tabTaskTitle: String? = null,
    @Json(name = "TAB_TASK_EMPTY_TITLE") val tabTaskEmptyTitle: String? = null,
    @Json(name = "TAB_TASK_EMPTY_SUBTITLE") val tabTaskEmptySubtitle: String? = null,
    @Json(name = "TAB_TASK_EMPTY_BUTTON") val tabTaskEmptyButton: String? = null,
    @Json(name = "TAB_USER_DATA") val tabUserData: String? = null,
    @Json(name = "TAB_USER_DATA_TITLE") val tabUserDataTitle: String? = null,
    @Json(name = "TAB_STUDY_INFO") val tabStudyInfo: String? = null,
    @Json(name = "TAB_STUDY_INFO_TITLE") val tabStudyInfoTitle: String? = null,

    @Json(name = "ACTIVITY_BUTTON_DEFAULT") val activityButtonDefault: String? = null,
    @Json(name = "QUICK_ACTIVITY_BUTTON_DEFAULT") val quickActivityButtonDefault: String? = null,

    @Json(name = "URL_PRIVACY_POLICY") val urlPrivacyPolicy: String? = null,
    @Json(name = "URL_TERMS_OF_SERVICE") val urlTermsOfService: String? = null,

    @Json(name = "VIDEO_DIARY_INTRO_TITLE") val videoDiaryIntroTitle: String? = null,
    @Json(name = "VIDEO_DIARY_INTRO_BUTTON") val videoDiaryIntroButton: String? = null,
    @Json(name = "VIDEO_DIARY_INTRO_PARAGRAPH_TITLE_A") val videoDiaryIntroParagraphTitleA: String? = null,
    @Json(name = "VIDEO_DIARY_INTRO_PARAGRAPH_BODY_A") val videoDiaryIntroParagraphBodyA: String? = null,
    @Json(name = "VIDEO_DIARY_INTRO_PARAGRAPH_TITLE_B") val videoDiaryIntroParagraphTitleB: String? = null,
    @Json(name = "VIDEO_DIARY_INTRO_PARAGRAPH_BODY_B") val videoDiaryIntroParagraphBodyB: String? = null,
    @Json(name = "VIDEO_DIARY_INTRO_PARAGRAPH_TITLE_C") val videoDiaryIntroParagraphTitleC: String? = null,
    @Json(name = "VIDEO_DIARY_INTRO_PARAGRAPH_BODY_C") val videoDiaryIntroParagraphBodyC: String? = null,
    @Json(name = "VIDEO_DIARY_RECORDER_INFO_TITLE") val videoDiaryRecorderInfoTitle: String? = null,
    @Json(name = "VIDEO_DIARY_RECORDER_INFO_BODY") val videoDiaryRecorderInfoBody: String? = null,
    @Json(name = "VIDEO_DIARY_RECORDER_TITLE") val videoDiaryRecorderTitle: String? = null,
    @Json(name = "VIDEO_DIARY_RECORDER_CLOSE_BUTTON") val videoDiaryRecorderCloseButton: String? = null,
    @Json(name = "VIDEO_DIARY_RECORDER_REVIEW_BUTTON") val videoDiaryRecorderReviewButton: String? = null,
    @Json(name = "VIDEO_DIARY_RECORDER_SUBMIT_BUTTON") val videoDiaryRecorderSubmitButton: String? = null,
    @Json(name = "VIDEO_DIARY_SUCCESS_TITLE") val videoDiarySuccessTitle: String? = null,
    @Json(name = "VIDEO_DIARY_DISCARD_TITLE") val videoDiaryDiscardTitle: String? = null,
    @Json(name = "VIDEO_DIARY_DISCARD_BODY") val videoDiaryDiscardBody: String? = null,
    @Json(name = "VIDEO_DIARY_DISCARD_CANCEL") val videoDiaryDiscardCancel: String? = null,
    @Json(name = "VIDEO_DIARY_DISCARD_CONFIRM") val videoDiaryDiscardConfirm: String? = null,
    @Json(name = "VIDEO_DIARY_MISSING_PERMISSION_DISCARD") val videoDiaryMissingPermissionDiscard: String? = null,
    @Json(name = "VIDEO_DIARY_MISSING_PERMISSION_TITLE_MIC") val videoDiaryMissingPermissionTitleMic: String? = null,
    @Json(name = "VIDEO_DIARY_MISSING_PERMISSION_BODY_MIC") val videoDiaryMissingPermissionBodyMic: String? = null,
    @Json(name = "VIDEO_DIARY_MISSING_PERMISSION_TITLE_CAMERA") val videoDiaryMissingPermissionTitleCamera: String? = null,
    @Json(name = "VIDEO_DIARY_MISSING_PERMISSION_BODY_CAMERA") val videoDiaryMissingPermissionBodyCamera: String? = null,
    @Json(name = "VIDEO_DIARY_MISSING_PERMISSION_SETTINGS") val videoDiaryMissingPermissionBodySettings: String? = null,
    @Json(name = "VIDEO_DIARY_RECORDER_START_RECORDING_DESCRIPTION") val videoDiaryRecorderStartRecordingDescription: String? = null,
    @Json(name = "VIDEO_DIARY_RECORDER_RESUME_RECORDING_DESCRIPTION") val videoDiaryRecorderResumeRecordingDescription: String? = null,
    @Json(name = "STUDY_INFO_CONTACT_INFO") val studyInfoContactInfo: String? = null,
    @Json(name = "STUDY_INFO_REWARDS") val studyInfoRewards: String?= null,
    @Json(name = "STUDY_INFO_FAQ") val studyInfoFaq: String? = null,

    @Json(name = "ERROR_TITLE_DEFAULT") val errorTitleDefault: String? = null,
    @Json(name = "ERROR_MESSAGE_DEFAULT") val errorMessageDefault: String? = null,
    @Json(name = "ERROR_BUTTON_RETRY") val errorButtonRetry: String? = null,
    @Json(name = "ERROR_BUTTON_CANCEL") val errorButtonCancel: String? = null,
    @Json(name = "ERROR_MESSAGE_REMOTE_SERVER") val errorMessageRemoteServer: String? = null,
    @Json(name = "ERROR_MESSAGE_CONNECTIVITY") val errorMessageConnectivity: String? = null

) {

    fun toText(): Option<Text> {

        val error = toError()
        val url = toUrl()
        val welcome = toWelcome()
        val intro = toIntro()
        val signUpLater = toSignUpLater()
        val phoneVerification = toPhoneVerification()
        val onboarding = toOnboardig()
        val tab = toTab()
        val activity = toActivity()
        val videoDiary = toVideoDiary()
        val studyInfo = toStudyInfo()

        return Option.fx {
            Text(
                error.bind(),
                url.bind(),
                welcome.bind(),
                intro.bind(),
                signUpLater.bind(),
                phoneVerification.bind(),
                onboarding.bind(),
                tab.bind(),
                activity.bind(),
                videoDiary.bind(),
                studyInfo.bind()
            )
        }
    }

    private fun toError(): Option<Error> =
        Option.fx {
            Error(
                errorTitleDefault.toOption().bind(),
                errorMessageDefault.toOption().bind(),
                errorButtonRetry.toOption().bind(),
                errorButtonCancel.toOption().bind(),
                errorMessageRemoteServer.toOption().bind(),
                errorMessageConnectivity.toOption().bind()
            )
        }

    private fun toUrl(): Option<Url> =
        Option.fx {
            Url(
                urlPrivacyPolicy.toOption().bind(),
                urlTermsOfService.toOption().bind()
            )
        }

    private fun toWelcome(): Option<Welcome> =
        Option.fx {
            Welcome(welcomeStartButton.toOption().bind())
        }

    private fun toIntro(): Option<Intro> =
        Option.fx {
            Intro(
                introTitle.toOption().bind(),
                introBody.toOption().bind(),
                introBack.toOption().bind(),
                introLogin.toOption().bind()
            )
        }

    private fun toSignUpLater(): Option<SignUpLater> =
        Option.fx {
            SignUpLater(
                setupLaterBody.toOption().bind(),
                setupLaterConfirmButton.toOption().bind()
            )
        }

    private fun toPhoneVerification(): Option<PhoneVerification> {

        val phoneVerificationError = toPhoneVerificationError()

        return Option.fx {
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
                phoneVerificationError.bind()
            )
        }
    }

    private fun toPhoneVerificationError(): Option<PhoneVerificationError> =
        Option.fx {
            PhoneVerificationError(
                phoneVerificationErrorMissingNumber.toOption().bind(),
                phoneVerificationErrorWrongCode.toOption().bind()
            )
        }

    private fun toOnboardig(): Option<Onboarding> {

        val onboardingUser = toOnboardingUser()
        val onboardingOptIn = toOnboardingOptIn()
        val onboardingIntegration = toOnboardingIntegration()

        return Option.fx {

            Onboarding(
                onboardingAbortTitle.toOption().bind(),
                onboardingAbortButton.toOption().bind(),
                onboardingAbortCancel.toOption().bind(),
                onboardingAbortConfirm.toOption().bind(),
                onboradingAbortMessage.toOption().bind(),
                onboradingAgreeButton.toOption().bind(),
                onboradingDisagreeButton.toOption().bind(),
                onboardingUser.bind(),
                onboardingOptIn.bind(),
                onboardingIntegration.bind()
            )
        }
    }

    private fun toOnboardingUser(): Option<OnboardingUser> {

        val onboardingUserError = toOnboardingUserError()

        return Option.fx {
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
                onboardingUserError.bind()
            )
        }
    }

    private fun toOnboardingUserError(): Option<OnboardingUserError> =
        Option.fx {
            OnboardingUserError(onboardingEmailVerificationWrongCode.toOption().bind())
        }

    private fun toOnboardingOptIn(): Option<OnboardingOptIn> =
        Option.fx {
            OnboardingOptIn(
                onboardingOptInSubmitButton.toOption().bind(),
                onboardingOptInMandatoryClose.toOption().bind(),
                onboardingOptInMandatoryTitle.toOption().bind(),
                onboardingOptInMandatoryDefault.toOption().bind()
            )
        }

    private fun toOnboardingIntegration(): Option<OnboardingIntegration> =
        Option.fx {
            OnboardingIntegration(
                onboardingIntegrationDownloadButtonDefault.toOption().bind(),
                onboardingIntegrationOpenAppButtonDefault.toOption().bind(),
                onboardingIntegrationLoginButtonDefault.toOption().bind(),
                onboardingIntegrationNextButtonDefault.toOption().bind()
            )
        }

    private fun toTab(): Option<Tab> =
        Option.fx {
            Tab(
                tabFeed.toOption().bind(),
                tabTask.toOption().bind(),
                tabTaskTitle.toOption().bind(),
                tabTaskEmptyTitle.toOption().bind(),
                tabTaskEmptySubtitle.toOption().bind(),
                tabTaskEmptyButton.toOption().bind(),
                tabUserData.toOption().bind(),
                tabUserDataTitle.toOption().bind(),
                tabStudyInfo.toOption().bind(),
                tabStudyInfoTitle.toOption().bind()
            )
        }

    private fun toActivity(): Option<Activity> =
        Option.fx {
            Activity(
                activityButtonDefault.toOption().bind(),
                quickActivityButtonDefault.toOption().bind()
            )
        }

    private fun toVideoDiary(): Option<VideoDiary> =
        Option.fx {
            VideoDiary(
                videoDiaryIntroTitle.toOption().bind(),
                videoDiaryIntroButton.toOption().bind(),
                videoDiaryIntroParagraphTitleA.toOption().bind(),
                videoDiaryIntroParagraphBodyA.toOption().bind(),
                videoDiaryIntroParagraphTitleB.toOption().bind(),
                videoDiaryIntroParagraphBodyB.toOption().bind(),
                videoDiaryIntroParagraphTitleC.toOption().bind(),
                videoDiaryIntroParagraphBodyC.toOption().bind(),
                videoDiaryRecorderInfoTitle.toOption().bind(),
                videoDiaryRecorderInfoBody.toOption().bind(),
                videoDiaryRecorderTitle.toOption().bind(),
                videoDiaryRecorderCloseButton.toOption().bind(),
                videoDiaryRecorderReviewButton.toOption().bind(),
                videoDiaryRecorderSubmitButton.toOption().bind(),
                videoDiarySuccessTitle.toOption().bind(),
                videoDiaryDiscardTitle.toOption().bind(),
                videoDiaryDiscardBody.toOption().bind(),
                videoDiaryDiscardCancel.toOption().bind(),
                videoDiaryDiscardConfirm.toOption().bind(),
                videoDiaryMissingPermissionDiscard.toOption().bind(),
                videoDiaryMissingPermissionTitleMic.toOption().bind(),
                videoDiaryMissingPermissionBodyMic.toOption().bind(),
                videoDiaryMissingPermissionTitleCamera.toOption().bind(),
                videoDiaryMissingPermissionBodyCamera.toOption().bind(),
                videoDiaryMissingPermissionBodySettings.toOption().bind(),
                videoDiaryRecorderStartRecordingDescription.toOption().bind(),
                videoDiaryRecorderResumeRecordingDescription.toOption().bind(),
            )
        }

    private fun toStudyInfo(): Option<StudyInfo> =
        Option.fx {
            StudyInfo(
                studyInfoContactInfo.toOption().bind(),
                studyInfoRewards.toOption().bind(),
                studyInfoFaq.toOption().bind()
            )
        }
}