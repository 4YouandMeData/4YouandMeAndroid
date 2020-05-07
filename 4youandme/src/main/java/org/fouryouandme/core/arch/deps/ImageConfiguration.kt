package org.fouryouandme.core.arch.deps

import androidx.annotation.DrawableRes

interface ImageConfiguration {

    @DrawableRes
    fun back(): Int

    @DrawableRes
    fun logo(): Int

    @DrawableRes
    fun logoStudy(): Int

    @DrawableRes
    fun welcome(): Int

    @DrawableRes
    fun signUp(): Int

}