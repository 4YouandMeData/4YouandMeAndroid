package org.fouryouandme.app

import org.fouryouandme.core.arch.deps.ImageConfiguration

class SampleImageConfiguration : ImageConfiguration {

    /* --- common ---*/

    override fun loading(): Int = R.drawable.loading

    override fun back(): Int = R.drawable.back

    override fun backSecondary(): Int = R.drawable.back_secondary

    override fun close(): Int = R.drawable.close

    override fun closeSecondary(): Int = R.drawable.close_secondary

    override fun clear(): Int = R.drawable.clear

    override fun logo(): Int = R.drawable.logo

    override fun logoStudy(): Int = R.drawable.logo_study

    override fun logoStudySecondary(): Int = R.drawable.logo_study_secondary

    /* --- auth --- */

    override fun entryWrong(): Int = R.drawable.phone_wrong

    override fun entryValid(): Int = R.drawable.phone_valid

    override fun signUpNextStep(): Int = R.drawable.sign_up_next

    override fun signUpNextStepSecondary(): Int = R.drawable.sign_up_next_secondary

    override fun signUpPreviousStepSecondary(): Int = R.drawable.sign_up_previous_secondary

    /* --- main --- */

    override fun tabFeed(): Int = R.drawable.tab_feed

    override fun tabTask(): Int = R.drawable.tab_task

    override fun tabUserData(): Int = R.drawable.tab_user_data

    override fun tabStudyInfo(): Int = R.drawable.tab_study_info

    /* --- task --- */

    override fun timer(): Int = R.drawable.timer

    override fun pocket(): Int = R.drawable.pocket

    override fun videoDiaryIntro(): Int = R.drawable.video_diary_intro

    override fun videoDiaryTime(): Int = R.drawable.video_diary_time

    override fun videoDiaryClose(): Int = R.drawable.video_diary_close

    override fun videoDiaryRecord(): Int = R.drawable.video_diary_record

    override fun videoDiaryPause(): Int = R.drawable.video_diary_pause

    override fun videoDiaryPlay(): Int = R.drawable.video_diary_play

    override fun videoDiaryFlashOn(): Int = R.drawable.video_diary_flash_on

    override fun videoDiaryFlashOff(): Int = R.drawable.video_diary_flash_off

    override fun videoDiaryToggleCamera(): Int = R.drawable.video_diary_toggle_camera

    override fun heartBeat(): Int = R.drawable.heartbeat

    override fun sittingMan(): Int = R.drawable.sittingman

    override fun walkingMan(): Int = R.drawable.walkingman

    /* --- menu item --- */

    override fun contactInfo(): Int = R.drawable.contact_info

    override fun rewards(): Int = R.drawable.rewards

    override fun faq(): Int = R.drawable.faq

    override fun arrow(): Int = R.drawable.arrow

    override fun pregnancy(): Int = R.drawable.your_pregnancy

    override fun devices(): Int = R.drawable.devices

    override fun reviewConsent(): Int = R.drawable.review_consent

    override fun permissions(): Int = R.drawable.permission

    override fun fitbit(): Int = R.drawable.fitbit

    override fun oura(): Int = R.drawable.oura

    override fun deactivatedButton(): Int = R.drawable.button_circle_next_primary_deactivated

    override fun location(): Int = R.drawable.location

    override fun pushNotification(): Int = R.drawable.push_notifications

    override fun editContainer(): Int = R.drawable.edit_container

    override fun pencil(): Int = R.drawable.pencil

}