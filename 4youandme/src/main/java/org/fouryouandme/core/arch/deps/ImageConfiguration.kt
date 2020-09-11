package org.fouryouandme.core.arch.deps

import androidx.annotation.DrawableRes

interface ImageConfiguration {

    /* --- common ---*/

    @DrawableRes
    fun loading(): Int

    @DrawableRes
    fun back(): Int

    @DrawableRes
    fun backSecondary(): Int

    @DrawableRes
    fun close(): Int

    @DrawableRes
    fun clear(): Int

    @DrawableRes
    fun logo(): Int

    @DrawableRes
    fun logoStudy(): Int

    @DrawableRes
    fun logoStudySecondary(): Int

    /* --- auth --- */

    @DrawableRes
    fun entryWrong(): Int

    @DrawableRes
    fun entryValid(): Int

    @DrawableRes
    fun signUpNextStep(): Int

    @DrawableRes
    fun signUpNextStepSecondary(): Int

    @DrawableRes
    fun signUpPreviousStepSecondary(): Int

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
    fun videoDiary(): Int

}