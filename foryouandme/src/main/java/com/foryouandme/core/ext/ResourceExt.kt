package com.foryouandme.core.ext

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.foryouandme.entity.resources.TextResource

/* --- text resource --- */

fun String?.toTextResource(@StringRes or: Int, vararg args: Any): TextResource =
    if(this != null) TextResource.Text(this) else TextResource.AndroidRes(or, args.toList())

@Composable
fun TextResource.getText(): String =
    when (this) {
        is TextResource.AndroidRes -> stringResource(id = resId, *args.toTypedArray())
        is TextResource.Text -> string
    }