package com.foryouandme.core.arch.deps

import androidx.annotation.RawRes

interface VideoConfiguration {

    @RawRes
    fun introVideo(): Int

    companion object {

        fun mock(): VideoConfiguration =
            object : VideoConfiguration {

                override fun introVideo(): Int = 0

            }

    }

}