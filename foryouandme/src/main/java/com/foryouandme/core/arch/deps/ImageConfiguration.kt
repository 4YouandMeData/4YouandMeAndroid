package com.foryouandme.core.arch.deps

import androidx.annotation.DrawableRes
import com.foryouandme.entity.resources.ImageResource

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

}