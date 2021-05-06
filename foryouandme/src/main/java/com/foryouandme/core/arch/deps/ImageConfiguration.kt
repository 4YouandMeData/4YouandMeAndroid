package com.foryouandme.core.arch.deps

import androidx.annotation.DrawableRes
import com.foryouandme.R

interface ImageConfiguration {

    /* --- common ---*/

    @DrawableRes
    fun splashLogo(): Int

    @DrawableRes
    fun pushSmallIcon(): Int

    @DrawableRes
    fun loading(): Int

    @DrawableRes
    fun back(): Int

    @DrawableRes
    fun backSecondary(): Int

    @DrawableRes
    fun close(): Int

    @DrawableRes
    fun closeSecondary(): Int

    @DrawableRes
    fun clear(): Int

    @DrawableRes
    fun logo(): Int

    @DrawableRes
    fun logoStudy(): Int

    @DrawableRes
    fun logoStudySecondary(): Int

    @DrawableRes
    fun nextStep(): Int

    @DrawableRes
    fun nextStepSecondary(): Int

    @DrawableRes
    fun previousStepSecondary(): Int

    /* --- auth --- */

    @DrawableRes
    fun entryWrong(): Int

    @DrawableRes
    fun entryValid(): Int

    /* --- main --- */

    @DrawableRes
    fun tabFeed(): Int

    @DrawableRes
    fun tabTask(): Int

    @DrawableRes
    fun tabUserData(): Int

    @DrawableRes
    fun tabStudyInfo(): Int

    /* --- task --- */

    @DrawableRes
    fun timer(): Int

    @DrawableRes
    fun pocket(): Int

    @DrawableRes
    fun videoDiaryIntro(): Int

    @DrawableRes
    fun videoDiaryTime(): Int

    @DrawableRes
    fun videoDiaryClose(): Int

    @DrawableRes
    fun videoDiaryRecord(): Int

    @DrawableRes
    fun videoDiaryPause(): Int

    @DrawableRes
    fun videoDiaryPlay(): Int

    @DrawableRes
    fun videoDiaryFlashOn(): Int

    @DrawableRes
    fun videoDiaryFlashOff(): Int

    @DrawableRes
    fun videoDiaryToggleCamera(): Int

    @DrawableRes
    fun heartBeat(): Int

    @DrawableRes
    fun sittingMan(): Int

    @DrawableRes
    fun walkingMan(): Int

    @DrawableRes
    fun phoneShake(): Int

    @DrawableRes
    fun phoneShakeCircle(): Int

    @DrawableRes
    fun trailMaking(): Int

    /* --- menu item --- */

    @DrawableRes
    fun aboutYou(): Int

    @DrawableRes
    fun contactInfo(): Int

    @DrawableRes
    fun rewards(): Int

    @DrawableRes
    fun faq(): Int

    @DrawableRes
    fun arrow(): Int

    @DrawableRes
    fun pregnancy(): Int

    @DrawableRes
    fun devices(): Int

    @DrawableRes
    fun reviewConsent(): Int

    @DrawableRes
    fun permissions(): Int

    @DrawableRes
    fun dailySurveyTime(): Int

    /*--- device item ---*/

    @DrawableRes
    fun smartwatch(): Int

    @DrawableRes
    fun oura(): Int

    @DrawableRes
    fun instagram(): Int

    @DrawableRes
    fun rescuetime(): Int

    @DrawableRes
    fun twitter(): Int

    @DrawableRes
    fun garmin(): Int

    @DrawableRes
    fun deactivatedButton(): Int

    /*--- permissions item ---*/

    @DrawableRes
    fun location(): Int

    @DrawableRes
    fun pushNotification(): Int

    /*--- your pregnancy ---*/

    @DrawableRes
    fun editContainer(): Int

    @DrawableRes
    fun pencil(): Int

    /* --- mock --- */

    companion object {

        fun mock(): ImageConfiguration =
            object: ImageConfiguration {
                override fun splashLogo(): Int = R.drawable.placeholder
                override fun pushSmallIcon(): Int = R.drawable.placeholder
                override fun loading(): Int = R.drawable.placeholder
                override fun back(): Int = R.drawable.placeholder
                override fun backSecondary(): Int = R.drawable.placeholder
                override fun close(): Int = R.drawable.placeholder
                override fun closeSecondary(): Int = R.drawable.placeholder
                override fun clear(): Int = R.drawable.placeholder
                override fun logo(): Int = R.drawable.placeholder
                override fun logoStudy(): Int = R.drawable.placeholder
                override fun logoStudySecondary(): Int = R.drawable.placeholder
                override fun nextStep(): Int = R.drawable.placeholder
                override fun nextStepSecondary(): Int = R.drawable.placeholder
                override fun previousStepSecondary(): Int = R.drawable.placeholder
                override fun entryWrong(): Int = R.drawable.placeholder
                override fun entryValid(): Int = R.drawable.placeholder
                override fun tabFeed(): Int = R.drawable.placeholder
                override fun tabTask(): Int = R.drawable.placeholder
                override fun tabUserData(): Int = R.drawable.placeholder
                override fun tabStudyInfo(): Int = R.drawable.placeholder
                override fun timer(): Int = R.drawable.placeholder
                override fun pocket(): Int = R.drawable.placeholder
                override fun videoDiaryIntro(): Int = R.drawable.placeholder
                override fun videoDiaryTime(): Int = R.drawable.placeholder
                override fun videoDiaryClose(): Int = R.drawable.placeholder
                override fun videoDiaryRecord(): Int = R.drawable.placeholder
                override fun videoDiaryPause(): Int = R.drawable.placeholder
                override fun videoDiaryPlay(): Int = R.drawable.placeholder
                override fun videoDiaryFlashOn(): Int = R.drawable.placeholder
                override fun videoDiaryFlashOff(): Int = R.drawable.placeholder
                override fun videoDiaryToggleCamera(): Int = R.drawable.placeholder
                override fun heartBeat(): Int = R.drawable.placeholder
                override fun sittingMan(): Int = R.drawable.placeholder
                override fun walkingMan(): Int = R.drawable.placeholder
                override fun phoneShake(): Int = R.drawable.placeholder
                override fun phoneShakeCircle(): Int = R.drawable.placeholder
                override fun trailMaking(): Int = R.drawable.placeholder
                override fun aboutYou(): Int = R.drawable.placeholder
                override fun contactInfo(): Int = R.drawable.placeholder
                override fun rewards(): Int = R.drawable.placeholder
                override fun faq(): Int = R.drawable.placeholder
                override fun arrow(): Int = R.drawable.placeholder
                override fun pregnancy(): Int = R.drawable.placeholder
                override fun devices(): Int = R.drawable.placeholder
                override fun reviewConsent(): Int = R.drawable.placeholder
                override fun permissions(): Int = R.drawable.placeholder
                override fun dailySurveyTime(): Int = R.drawable.placeholder
                override fun smartwatch(): Int = R.drawable.placeholder
                override fun oura(): Int = R.drawable.placeholder
                override fun instagram(): Int = R.drawable.placeholder
                override fun rescuetime(): Int = R.drawable.placeholder
                override fun twitter(): Int = R.drawable.placeholder
                override fun garmin(): Int = R.drawable.placeholder
                override fun deactivatedButton(): Int = R.drawable.placeholder
                override fun location(): Int = R.drawable.placeholder
                override fun pushNotification(): Int = R.drawable.placeholder
                override fun editContainer(): Int = R.drawable.placeholder
                override fun pencil(): Int = R.drawable.placeholder
            }

    }

}