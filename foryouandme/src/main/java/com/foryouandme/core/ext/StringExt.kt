package com.foryouandme.core.ext

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

fun String?.emptyOrBlankToNull(): String? =
    if (isNullOrBlank() || isNullOrEmpty()) null
    else this

fun String.decodeBase64Image(): Bitmap? =
    catchToNull {

        val byteArray = Base64.decode(this, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

    }