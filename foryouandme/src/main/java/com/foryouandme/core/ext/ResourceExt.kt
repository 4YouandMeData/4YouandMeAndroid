package com.foryouandme.core.ext

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.foryouandme.entity.resources.TextResource

/* --- text resource --- */

fun String?.toTextResource(@StringRes or: Int, vararg args: Any): TextResource =
    if(this != null) TextResource.Text(this) else TextResource.AndroidRes(or, args.toList())

fun Int.toTextResource(vararg args: Any): TextResource =
    TextResource.AndroidRes(this, args.toList())

@Composable
fun TextResource.getText(): String =
    when (this) {
        is TextResource.AndroidRes -> stringResource(id = resId, *args.toTypedArray())
        is TextResource.Text -> string
    }

fun TextResource.getText(context: Context): String =
    when (this) {
        is TextResource.AndroidRes -> context.getString(resId, *args.toTypedArray())
        is TextResource.Text -> string
    }