package org.fouryouandme.core.ext

import arrow.core.None
import arrow.core.Option
import arrow.core.toOption

fun String?.emptyOrBlankToNone(): Option<String> =
    if (isNullOrBlank() || isNullOrEmpty()) None
    else toOption()