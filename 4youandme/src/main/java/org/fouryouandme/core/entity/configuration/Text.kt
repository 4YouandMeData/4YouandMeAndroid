package org.fouryouandme.core.entity.configuration

data class Text(
    val error: Error,
    val url: Url,
    val welcome: Welcome,
    val intro: Intro,
    val signUpLater: SignUpLater,
    val phoneVerification: PhoneVerification,
    val onboarding: Onboarding,
    val tab: Tab,
    val activity: Activity,
    val videoDiary: VideoDiary,
    val studyInfo: StudyInfo,
    val profile: Profile,
    val yourData: YourData,
    val task: Task,
    val gaitActivity: GaitActivity,
    val fitnessActivity: FitnessActivity,
    val camCogActivity: CamCogActivity
)

data class Error(
    val titleDefault: String,
    val messageDefault: String,
    val buttonRetry: String,
    val buttonCancel: String,
    val messageRemoteServer: String,
    val messageConnectivity: String
)

data class Url(
    val privacy: String,
    val terms: String
)

data class Welcome(
    val startButton: String
)

data class Intro(
    val title: String,
    val body: String,
    val back: String,
    val login: String
)

data class SignUpLater(
    val body: String,
    val confirmButton: String
)

data class PhoneVerification(
    val title: String,
    val body: String,
    val legal: String,
    val numberDescription: String,
    val legalPrivacyPolicy: String,
    val legalTermsOfService: String,
    val resendCode: String,
    val wrongNumber: String,
    val codeTitle: String,
    val codeBody: String,
    val codeDescription: String,
    val error: PhoneVerificationError
)

data class PhoneVerificationError(
    val errorMissingNumber: String,
    val errorWrongCode: String
)

data class Onboarding(
    val abortTitle: String,
    val abortButton: String,
    val abortCancel: String,
    val abortConfirm: String,
    val abortMessage: String,
    val agreeButton: String,
    val disagreeButton: String,
    val user: OnboardingUser,
    val optIn: OnboardingOptIn,
    val integration: OnboardingIntegration
)

data class OnboardingUser(
    val nameTitle: String,
    val nameLastNameDescription: String,
    val nameFirstNameDescription: String,
    val signatureTitle: String,
    val signatureBody: String,
    val signatureClear: String,
    val signaturePlaceholder: String,
    val emailInfo: String,
    val emailDescription: String,
    val emailVerificationTitle: String,
    val emailVerificationBody: String,
    val emailVerificationCodeDescription: String,
    val emailVerificationResend: String,
    val emailVerificationWrongMail: String,
    val error: OnboardingUserError
)

data class OnboardingOptIn(
    val submitButton: String,
    val mandatoryClose: String,
    val mandatoryTitle: String,
    val mandatoryDefault: String
)

data class OnboardingIntegration(
    val downloadButtonDefault: String,
    val openAppDefault: String,
    val loginButtonDefault: String,
    val nextDefault: String
)

data class OnboardingUserError(
    val emailVerificationWrongCode: String
)

data class Tab(
    val feed: String,
    val feedTitle: String,
    val feedSubTitle: String,
    val feedEmptyTitle: String,
    val feedEmptySubTitle: String,
    val feedHeaderTitle: String,
    val feedHeaderSubTitle: String,
    val feedHeaderPoints: String,
    val tasks: String,
    val tasksTitle: String,
    val tabTaskEmptyTitle: String,
    val tabTaskEmptySubtitle: String,
    val tabTaskEmptyButton: String,
    val userData: String,
    val userDataTitle: String,
    val studyInfo: String,
    val studyInfoTitle: String
)

data class Activity(
    val activityButtonDefault: String,
    val quickActivityButtonDefault: String
)

data class VideoDiary(
    val introTitle: String,
    val introButton: String,
    val introParagraphTitleA: String,
    val introParagraphBodyA: String,
    val introParagraphTitleB: String,
    val introParagraphBodyB: String,
    val introParagraphTitleC: String,
    val introParagraphBodyC: String,
    val recorderInfoTitle: String,
    val recorderInfoBody: String,
    val recorderTitle: String,
    val recorderCloseButton: String,
    val recorderReviewButton: String,
    val recorderSubmitButton: String,
    val successTitle: String,
    val discardTitle: String,
    val discardBody: String,
    val discardCancel: String,
    val discardConfirm: String,
    val missingPermissionDiscard: String,
    val missingPermissionTitleMic: String,
    val missingPermissionBodyMic: String,
    val missingPermissionTitleCamera: String,
    val missingPermissionBodyCamera: String,
    val missingPermissionBodySettings: String,
    val recorderStartRecordingDescription: String,
    val recorderResumeRecordingDescription: String,
)

data class StudyInfo(
    val aboutYou: String,
    val contactInfo: String,
    val rewards: String,
    val faq: String,
)

data class Profile(
    val title: String,
    val firstItem: String,
    val secondItem: String,
    val thirdItem: String,
    val fourthItem: String,
    val disclaimer: String,
    val connect: String,
    val allow: String,
    val allowed: String,
    val edit: String,
    val submit: String,
    val oauth: String
)

data class YourData(
    val dataPeriodTitle: String,
    val periodDay: String,
    val periodWeek: String,
    val periodMonth: String,
    val periodYear: String
)

data class Task(
    val remindButton: String,
    val startButton: String
)

data class GaitActivity(
    val introTitle: String,
    val introBody: String,
)

data class FitnessActivity(
    val introTitle: String,
    val introBody: String
)

data class CamCogActivity(
    val introTitle: String,
    val introBody: String,
)