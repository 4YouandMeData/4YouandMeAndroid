package org.fouryouandme.core.arch.navigation

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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

fun alertAction(title: String, message: String, close: String): ActivityAction = {
    AlertDialog.Builder(it)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(close) { dialog, _ -> dialog.dismiss() }
        .show()
}