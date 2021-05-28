package com.foryouandme.core.ext

import java.io.File

fun File?.readJson(): String? {

    val stringBuilder = StringBuilder()

    return if (this != null) {

        forEachLine {
            stringBuilder.append(it).append("\n")
        }

        // This response will have Json Format String
        stringBuilder.toString()

    } else null

}