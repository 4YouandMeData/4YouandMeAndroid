package com.foryouandme.core.entity.configuration.checkbox

import android.content.res.ColorStateList


fun checkbox(
    uncheckedColor: Int,
    checkedColor: Int
): ColorStateList =
    ColorStateList(
        arrayOf(
            intArrayOf(-android.R.attr.state_checked),
            intArrayOf(android.R.attr.state_checked)
        ), intArrayOf(
            uncheckedColor,
            checkedColor
        )
    )