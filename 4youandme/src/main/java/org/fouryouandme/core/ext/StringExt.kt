package org.fouryouandme.core.ext

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import arrow.core.None
import arrow.core.Option
import arrow.core.toOption
import arrow.fx.IO
import arrow.fx.extensions.io.concurrent.concurrent
import arrow.syntax.function.pipe

fun String?.emptyOrBlankToNone(): Option<String> =
    if (isNullOrBlank() || isNullOrEmpty()) None
    else toOption()

fun String.decodeBase64Image(): Option<Bitmap> =
    IO.concurrent().fx.concurrent {

        effect {

            Base64.decode(this@decodeBase64Image, Base64.DEFAULT)
                .pipe { BitmapFactory.decodeByteArray(it, 0, it.size) }

        }.attempt().bind().toOption()

    }.unsafeRunSync()
