package org.fouryouandme.core.arch.deps

import androidx.annotation.DrawableRes

interface ImageConfiguration {

    @DrawableRes
    fun logo(): Int

    @DrawableRes
    fun welcome(): Int

}