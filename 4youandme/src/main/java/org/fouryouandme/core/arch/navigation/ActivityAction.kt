package org.fouryouandme.core.arch.navigation

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import org.fouryouandme.core.arch.error.FourYouAndMeError

typealias ActivityAction = (FragmentActivity) -> Unit

fun toastAction(error: FourYouAndMeError): ActivityAction = {
    showToast(it, error.message(it))
}

fun toastAction(message: String): ActivityAction = {
    showToast(it, message)
}

private fun showToast(context: Context, message: String): Unit =
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()