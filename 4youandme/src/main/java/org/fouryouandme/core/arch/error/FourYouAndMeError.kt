package org.fouryouandme.core.arch.error

import android.content.Context

sealed class FourYouAndMeError(val message: (Context) -> String)