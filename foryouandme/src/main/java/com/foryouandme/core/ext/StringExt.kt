package com.foryouandme.core.ext

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import arrow.core.Either
import arrow.syntax.function.pipe

fun String?.emptyOrBlankToNull(): String? =
    if (isNullOrBlank() || isNullOrEmpty()) null
    else this

suspend fun String.decodeBase64ImageFx(): Either<Throwable, Bitmap> =
    Either.catch {

        Base64.decode(this, Base64.DEFAULT)
            .pipe { BitmapFactory.decodeByteArray(it, 0, it.size) }


    }