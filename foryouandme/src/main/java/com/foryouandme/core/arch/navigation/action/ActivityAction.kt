package com.foryouandme.core.arch.navigation.action

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import arrow.core.getOrElse
import arrow.core.toOption
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.arch.navigation.Navigator
import com.foryouandme.core.ext.startCoroutineAsync

typealias ActivityAction = (Context) -> Unit

fun toastAction(error: ForYouAndMeError): ActivityAction = {
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

private const val storeIntent =
    "https://play.google.com/store/apps/details?id="

fun playStoreAction(packageName: String): ActivityAction = {

    val intent =
        Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("$storeIntent$packageName")
            setPackage("com.android.vending")
        }

    it.startActivity(intent)

}

fun openApp(packageName: String): ActivityAction = {

    val intent = it.packageManager.getLaunchIntentForPackage(packageName)
        .toOption()
        .getOrElse {
            Intent(Intent.ACTION_VIEW)
                .apply {
                    data = Uri.parse("$storeIntent$packageName")
                    setPackage("com.android.vending")
                }
        }

    it.startActivity(intent)

}

fun permissionSettingsAction(): ActivityAction = {

    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.parse("package:${it.packageName}")
    )
    intent.addCategory(Intent.CATEGORY_DEFAULT)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    it.startActivity(intent)

}

fun permissionSettingsDialogAction(
    navigator: Navigator,
    title: String,
    description: String,
    settings: String,
    cancel: String,
    isCancelable: Boolean,
    onSettings: suspend () -> Unit,
    onCancel: suspend () -> Unit
): ActivityAction = {
    AlertDialog.Builder(it)
        .setTitle(title)
        .setMessage(description)
        .setPositiveButton(settings) { _, _ ->
            startCoroutineAsync {
                navigator.performActionSuspend(permissionSettingsAction())
                onSettings()
            }
        }
        .setNegativeButton(cancel) { _, _ ->
            startCoroutineAsync { onCancel() }
        }
        .setCancelable(isCancelable)
        .show()
}

