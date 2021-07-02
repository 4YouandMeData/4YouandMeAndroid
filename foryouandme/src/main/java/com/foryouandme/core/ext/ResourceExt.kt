package com.foryouandme.core.ext

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.foryouandme.entity.source.ColorSource
import com.foryouandme.entity.source.TextSource
import com.foryouandme.ui.compose.toColor

/* --- text resource --- */

fun String.toTextSource(): TextSource =
    TextSource.Text(this)

fun String?.toTextSource(@StringRes or: Int, vararg args: Any): TextSource =
    if (this != null) TextSource.Text(this) else TextSource.AndroidRes(or, args.toList())

fun Int.toTextSource(vararg args: Any): TextSource =
    TextSource.AndroidRes(this, args.toList())

@Composable
fun TextSource.getText(): String =
    when (this) {
        is TextSource.AndroidRes -> stringResource(id = resId, *args.toTypedArray())
        is TextSource.Text -> string
    }

fun TextSource.getText(context: Context): String =
    when (this) {
        is TextSource.AndroidRes -> context.getString(resId, *args.toTypedArray())
        is TextSource.Text -> string
    }

/* --- color resource --- */

fun ColorSource.getColor(): Color =
    when (this) {
        is ColorSource.AndroidRes -> resId.toColor()
    }

fun Int.toColorSource(): ColorSource =
    ColorSource.AndroidRes(this)