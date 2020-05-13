package org.fouryouandme.core.arch.deps

import androidx.annotation.DrawableRes

interface ImageConfiguration {

    /* --- common ---*/

    @DrawableRes
    fun back(): Int

    @DrawableRes
    fun logo(): Int

    @DrawableRes
    fun logoStudy(): Int

    /* --- auth --- */

    @DrawableRes
    fun welcome(): Int

    @DrawableRes
    fun signUp(): Int

    @DrawableRes
    fun phoneWrong(): Int

    @DrawableRes
    fun phoneValid(): Int
}