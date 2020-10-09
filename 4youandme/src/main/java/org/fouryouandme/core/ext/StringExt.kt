package org.fouryouandme.core.ext

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import arrow.core.Either
import arrow.core.None
import arrow.core.Option
import arrow.core.toOption
import arrow.fx.IO
import arrow.fx.extensions.io.concurrent.concurrent
import arrow.syntax.function.pipe

fun String?.emptyOrBlankToNone(): Option<String> =
    if (isNullOrBlank() || isNullOrEmpty()) None
    else toOption()

fun String?.emptyOrBlankToNull(): String? =
    if (isNullOrBlank() || isNullOrEmpty()) null
    else this

@Deprecated(message = "use suspend version")
fun String.decodeBase64Image(): Option<Bitmap> =
    IO.concurrent().fx.concurrent {

        effect {

            Base64.decode(this@decodeBase64Image, Base64.DEFAULT)
                .pipe { BitmapFactory.decodeByteArray(it, 0, it.size) }

        }.attempt().bind().toOption()

    }.unsafeRunSync()

suspend fun String.decodeBase64ImageFx(): Either<Throwable, Bitmap> =
    Either.catch {

        Base64.decode(this, Base64.DEFAULT)
            .pipe { BitmapFactory.decodeByteArray(it, 0, it.size) }


    }
