package org.fouryouandme.core.arch.navigation

import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import org.fouryouandme.core.arch.error.FourYouAndMeError

typealias ActivityAction = (FragmentActivity) -> Unit

fun toastAction(error: FourYouAndMeError): ActivityAction = {

    Toast.makeText(it, error.message(it), Toast.LENGTH_LONG).show()

}