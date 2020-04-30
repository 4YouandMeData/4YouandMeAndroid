package org.fouryouandme.core.arch.error

import android.content.Context
import org.fouryouandme.R

sealed class FourYouAndMeError(val message: (Context) -> String) {

    /* --- generic --- */

    object Unkonwn : FourYouAndMeError({ it.getString(R.string.ERROR_generic) })

}