package com.foryouandme.core.data.api.configuration.response

import arrow.core.Either
import com.foryouandme.entity.configuration.*
import com.squareup.moshi.Json

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

    @Json(name = "INTRO_VIDEO_CONTINUE_BUTTON") val introVideoContinueButton: String? = null,

    @Json(name = "ONBOARDING_SECTION_LIST") val onboardingSectionList: String? = null,
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
    @Json(name = "TAB_FEED_TITLE") val tabFeedTitle: String? = null,
    @Json(name = "TAB_FEED_SUBTITLE") val tabFeedSubTitle: String? = null,
    @Json(name = "TAB_FEED_EMPTY_TITLE") val tabFeedEmptyTitle: String? = null,
    @Json(name = "TAB_FEED_EMPTY_SUBTITLE") val tabFeedEmptySubTitle: String? = null,
    @Json(name = "TAB_FEED_HEADER_TITLE") val tabFeedHeaderTitle: String? = null,
    @Json(name = "TAB_FEED_HEADER_SUBTITLE") val tabFeedHeaderSubTitle: String? = null,
    @Json(name = "TAB_FEED_HEADER_POINTS") val tabFeedHeaderPoints: String? = null,

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

    @Json(name = "EDUCATIONAL_BUTTON_DEFAULT") val educationalButtonDefault: String? = null,
    @Json(name = "REWARD_BUTTON_DEFAULT") val rewardButtonDefault: String? = null,
    @Json(name = "ALERT_BUTTON_DEFAULT") val alertButtonDefault: String? = null,

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

    @Json(name = "TASK_GAIT_INTRO_TITLE") val taskGaitIntroTitle: String? = null,
    @Json(name = "TASK_GAIT_INTRO_BODY") val taskGaitIntroBody: String? = null,

    @Json(name = "TASK_WALK_INTRO_TITLE") val taskWalkIntroTitle: String? = null,
    @Json(name = "TASK_WALK_INTRO_BODY") val taskWalkIntroBody: String? = null,

    @Json(name = "CAMCOG_TASK_TITLE") val camCogTaskTitle: String? = null,
    @Json(name = "CAMCOG_TASK_BODY") val camCogTaskBody: String? = null,

    @Json(name = "TASK_REMIND_ME_LATER") val taskRemindMeLater: String? = null,
    @Json(name = "TASK_START_BUTTON") val taskStartButton: String? = null,
    @Json(name = "SURVEY_BUTTON_SKIP") val surveyButtonSkip: String? = null,

    @Json(name = "STUDY_INFO_ABOUT_YOU") val studyInfoAboutYou: String? = null,
    @Json(name = "STUDY_INFO_CONTACT_INFO") val studyInfoContactInfo: String? = null,
    @Json(name = "STUDY_INFO_REWARDS") val studyInfoRewards: String? = null,
    @Json(name = "STUDY_INFO_FAQ") val studyInfoFaq: String? = null,

    @Json(name = "PROFILE_TITLE") val profileTitle: String? = null,

    @Json(name = "ABOUT_YOU_YOUR_PREGNANCY") val aboutYouYourPregnancy: String? = null,
    @Json(name = "ABOUT_YOU_APPS_AND_DEVICES") val aboutYouAppsAndDevices: String? = null,
    @Json(name = "ABOUT_YOU_REVIEW_CONSENT") val aboutYouReviewConsent: String? = null,
    @Json(name = "ABOUT_YOU_PERMISSIONS") val aboutYouPermissions: String? = null,
    @Json(name = "ABOUT_YOU_DISCLAIMER") val aboutYouDisclaimer: String? = null,

    @Json(name = "YOUR_APPS_AND_DEVICES_CONNECT") val yourAppsAndDevicesConnect: String? = null,

    @Json(name = "PERMISSIONS_ALLOW") val permissionsAllow: String? = null,
    @Json(name = "PERMISSIONS_ALLOWED") val permissionsAllowed: String? = null,
    @Json(name = "PERMISSION_DENIED") val permissionDenied: String? = null,
    @Json(name = "PERMISSION_CANCEL") val permissionCancel: String? = null,
    @Json(name = "PERMISSION_MESSAGE") val permissionMessage: String? = null,
    @Json(name = "PERMISSION_SETTINGS") val permissionSettings: String? = null,

    @Json(name = "PROFILE_USER_INFO_BUTTON_EDIT") val profileUserInfoButtonEdit: String? = null,
    @Json(name = "PROFILE_USER_INFO_BUTTON_SUBMIT") val profileUserInfoButtonSubmit: String? = null,

    @Json(name = "TAB_USER_DATA_PERIOD_TITLE") val tabUserDataPeriodTitle: String? = null,
    @Json(name = "TAB_USER_DATA_PERIOD_DAY") val tabUserDataPeriodDay: String? = null,
    @Json(name = "TAB_USER_DATA_PERIOD_WEEK") val tabUserDataPeriodWeek: String? = null,
    @Json(name = "TAB_USER_DATA_PERIOD_MONTH") val tabUserDataPeriodMonth: String? = null,
    @Json(name = "TAB_USER_DATA_PERIOD_YEAR") val tabUserDataPeriodYear: String? = null,

    @Json(name = "ERROR_TITLE_DEFAULT") val errorTitleDefault: String? = null,
    @Json(name = "ERROR_MESSAGE_DEFAULT") val errorMessageDefault: String? = null,
    @Json(name = "ERROR_BUTTON_RETRY") val errorButtonRetry: String? = null,
    @Json(name = "ERROR_BUTTON_CANCEL") val errorButtonCancel: String? = null,
    @Json(name = "ERROR_MESSAGE_REMOTE_SERVER") val errorMessageRemoteServer: String? = null,
    @Json(name = "ERROR_MESSAGE_CONNECTIVITY") val errorMessageConnectivity: String? = null,

    @Json(name = "OAUTH_AVAILABLE_INTERATIONS") val oauthAvailableInterations: String? = null

) {

    // TODO: refactor without option
    suspend fun toText(): Text? {

        val error = toError()
        val url = toUrl()
        val welcome = toWelcome()
        val intro = toIntro()
        val signUpLater = toSignUpLater()
        val phoneVerification = toPhoneVerification()
        val onboarding = toOnboardig()
        val tab = toTab()
        val activity = toActivity()
        val feed = toFeed()
        val videoDiary = toVideoDiary()
        val studyInfo = toStudyInfo()
        val profile = toProfile()
        val yourData = toYourData()
        val task = toTask()
        val gaitActivity = toGaitActivity()
        val fitnessActivity = toFitnessActivity()
        val camCogActivity = toCamCogActivity()

        return Either.catch {
            Text(
                error!!,
                url!!,
                welcome!!,
                intro!!,
                signUpLater!!,
                phoneVerification!!,
                onboarding!!,
                tab!!,
                activity!!,
                feed!!,
                videoDiary!!,
                studyInfo!!,
                profile!!,
                yourData!!,
                task!!,
                gaitActivity!!,
                fitnessActivity!!,
                camCogActivity!!
            )

        }.orNull()
    }

    private suspend fun toError(): Error? =
        Either.catch {

            Error(
                errorTitleDefault!!,
                errorMessageDefault!!,
                errorButtonRetry!!,
                errorButtonCancel!!,
                errorMessageRemoteServer!!,
                errorMessageConnectivity!!
            )

        }.orNull()

    private suspend fun toUrl(): Url? =
        Either.catch {

            Url(
                urlPrivacyPolicy!!,
                urlTermsOfService!!
            )

        }.orNull()

    private suspend fun toWelcome(): Welcome? =
        Either.catch { Welcome(welcomeStartButton!!) }.orNull()

    private suspend fun toIntro(): Intro? =
        Either.catch {

            Intro(
                introTitle!!,
                introBody!!,
                introBack!!,
                introLogin!!
            )

        }.orNull()

    private suspend fun toSignUpLater(): SignUpLater? =
        Either.catch {

            SignUpLater(
                setupLaterBody!!,
                setupLaterConfirmButton!!
            )

        }.orNull()

    private suspend fun toPhoneVerification(): PhoneVerification? {

        val phoneVerificationError = toPhoneVerificationError()

        return Either.catch {

            PhoneVerification(
                phoneVerificationTitle!!,
                phoneVerificationBody!!,
                phoneVerificationLegal!!,
                phoneVerificationNumberDescription!!,
                phoneVerificationLegalPrivacyPolicy!!,
                phoneVerificationLegalTermsOfService!!,
                phoneVerificationResendCode!!,
                phoneVerificationWrongNumber!!,
                phoneVerificationCodeTitle!!,
                phoneVerificationCodeBody!!,
                phoneVerificationCodeDescription!!,
                phoneVerificationError!!
            )

        }.orNull()
    }

    private suspend fun toPhoneVerificationError(): PhoneVerificationError? =
        Either.catch {

            PhoneVerificationError(

                phoneVerificationErrorMissingNumber!!,
                phoneVerificationErrorWrongCode!!
            )

        }.orNull()

    private suspend fun toOnboardig(): Onboarding? {

        val onboardingUser = toOnboardingUser()
        val onboardingOptIn = toOnboardingOptIn()
        val onboardingIntegration = toOnboardingIntegration()

        return Either.catch {

            Onboarding(
                onboardingSectionList!!.split(";"),
                introVideoContinueButton!!,
                onboardingAbortTitle!!,
                onboardingAbortButton!!,
                onboardingAbortCancel!!,
                onboardingAbortConfirm!!,
                onboradingAbortMessage!!,
                onboradingAgreeButton!!,
                onboradingDisagreeButton!!,
                onboardingUser!!,
                onboardingOptIn!!,
                onboardingIntegration!!
            )

        }.orNull()
    }

    private suspend fun toOnboardingUser(): OnboardingUser? {

        val onboardingUserError = toOnboardingUserError()

        return Either.catch {

            OnboardingUser(
                onboardingUserNameTitle!!,
                onboardingNameLastNameDescription!!,
                onboardingNameFirstNameDescription!!,
                onboardingSignatureTitle!!,
                onboardingSignatureBody!!,
                onboardingSignatureClear!!,
                onboardingSignaturePlaceholder!!,
                onboardingEmailInfo!!,
                onboardingEmailDescription!!,
                onboardingEmailVerificationTitle!!,
                onboardingEmailVerificationBody!!,
                onboardingEmailVerificationCodeDescription!!,
                onboardingEmailVerificationResend!!,
                onboardingEmailVerificationWrongMail!!,
                onboardingUserError!!
            )

        }.orNull()
    }

    private suspend fun toOnboardingUserError(): OnboardingUserError? =
        Either.catch {
            OnboardingUserError(onboardingEmailVerificationWrongCode!!)
        }.orNull()

    private suspend fun toOnboardingOptIn(): OnboardingOptIn? =
        Either.catch {

            OnboardingOptIn(
                onboardingOptInSubmitButton!!,
                onboardingOptInMandatoryClose!!,
                onboardingOptInMandatoryTitle!!,
                onboardingOptInMandatoryDefault!!
            )

        }.orNull()

    private suspend fun toOnboardingIntegration(): OnboardingIntegration? =
        Either.catch {

            OnboardingIntegration(
                onboardingIntegrationDownloadButtonDefault!!,
                onboardingIntegrationOpenAppButtonDefault!!,
                onboardingIntegrationLoginButtonDefault!!,
                onboardingIntegrationNextButtonDefault!!
            )

        }.orNull()

    private suspend fun toTab(): Tab? =
        Either.catch {

            Tab(
                tabFeed!!,
                tabFeedTitle!!,
                tabFeedSubTitle!!,
                tabFeedEmptyTitle!!,
                tabFeedEmptySubTitle!!,
                tabFeedHeaderTitle!!,
                tabFeedHeaderSubTitle!!,
                tabFeedHeaderPoints!!,
                tabTask!!,
                tabTaskTitle!!,
                tabTaskEmptyTitle!!,
                tabTaskEmptySubtitle!!,
                tabTaskEmptyButton!!,
                tabUserData!!,
                tabUserDataTitle!!,
                tabStudyInfo!!,
                tabStudyInfoTitle!!
            )

        }.orNull()

    private suspend fun toActivity(): Activity? =
        Either.catch {

            Activity(
                activityButtonDefault!!,
                quickActivityButtonDefault!!
            )

        }.orNull()

    private suspend fun toFeed(): Feed? =
        Either.catch {

            Feed(
                educationalButtonDefault!!,
                rewardButtonDefault!!,
                alertButtonDefault!!
            )

        }.orNull()

    private suspend fun toVideoDiary(): VideoDiary? =
        Either.catch {

            VideoDiary(
                videoDiaryIntroTitle!!,
                videoDiaryIntroButton!!,
                videoDiaryIntroParagraphTitleA!!,
                videoDiaryIntroParagraphBodyA!!,
                videoDiaryIntroParagraphTitleB!!,
                videoDiaryIntroParagraphBodyB!!,
                videoDiaryIntroParagraphTitleC!!,
                videoDiaryIntroParagraphBodyC!!,
                videoDiaryRecorderInfoTitle!!,
                videoDiaryRecorderInfoBody!!,
                videoDiaryRecorderTitle!!,
                videoDiaryRecorderCloseButton!!,
                videoDiaryRecorderReviewButton!!,
                videoDiaryRecorderSubmitButton!!,
                videoDiarySuccessTitle!!,
                videoDiaryDiscardTitle!!,
                videoDiaryDiscardBody!!,
                videoDiaryDiscardCancel!!,
                videoDiaryDiscardConfirm!!,
                videoDiaryMissingPermissionDiscard!!,
                videoDiaryMissingPermissionTitleMic!!,
                videoDiaryMissingPermissionBodyMic!!,
                videoDiaryMissingPermissionTitleCamera!!,
                videoDiaryMissingPermissionBodyCamera!!,
                videoDiaryMissingPermissionBodySettings!!,
                videoDiaryRecorderStartRecordingDescription!!,
                videoDiaryRecorderResumeRecordingDescription!!,
            )

        }.orNull()

    private suspend fun toStudyInfo(): StudyInfo? =
        Either.catch {

            StudyInfo(
                studyInfoAboutYou!!,
                studyInfoContactInfo!!,
                studyInfoRewards!!,
                studyInfoFaq!!
            )

        }.orNull()

    private suspend fun toProfile(): Profile? =
        Either.catch {

            Profile(
                profileTitle!!,
                aboutYouYourPregnancy!!,
                aboutYouAppsAndDevices!!,
                aboutYouReviewConsent!!,
                aboutYouPermissions!!,
                aboutYouDisclaimer!!,
                yourAppsAndDevicesConnect!!,
                permissionsAllow!!,
                permissionsAllowed!!,
                permissionDenied!!,
                permissionCancel!!,
                permissionMessage!!,
                permissionSettings!!,
                profileUserInfoButtonEdit!!,
                profileUserInfoButtonSubmit!!,
                oauthAvailableInterations!!
            )

        }.orNull()

    private suspend fun toYourData(): YourData? =
        Either.catch {

            YourData(
                tabUserDataPeriodTitle!!,
                tabUserDataPeriodDay!!,
                tabUserDataPeriodWeek!!,
                tabUserDataPeriodMonth!!,
                tabUserDataPeriodYear!!
            )

        }.orNull()

    private suspend fun toTask(): Task? =
        Either.catch {

            Task(
                taskRemindMeLater!!,
                taskStartButton!!,
                surveyButtonSkip!!
            )

        }.orNull()

    private suspend fun toGaitActivity(): GaitActivity? =
        Either.catch {

            GaitActivity(
                taskGaitIntroTitle!!,
                taskGaitIntroBody!!
            )

        }.orNull()

    private suspend fun toFitnessActivity(): FitnessActivity? =
        Either.catch {

            FitnessActivity(
                taskWalkIntroTitle!!,
                taskWalkIntroBody!!
            )

        }.orNull()

    private suspend fun toCamCogActivity(): CamCogActivity? =
        Either.catch {

            CamCogActivity(
                camCogTaskTitle!!,
                camCogTaskBody!!
            )

        }.orNull()
}