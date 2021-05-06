package com.foryouandme.core.arch.navigation.action

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import com.foryouandme.core.ext.execute

sealed class ContextAction(val block: (Context) -> Unit) {

    object PermissionSettingsAction : ContextAction(
        {

            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:${it.packageName}")
            )
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            it.startActivity(intent)

        }
    )

    data class PermissionSettingsDialogAction(
        val title: String,
        val description: String,
        val settings: String,
        val cancel: String,
        val isCancelable: Boolean,
        val onSettings: () -> Unit = {},
        val onCancel: () -> Unit = {}
    ) : ContextAction({
        AlertDialog.Builder(it)
            .setTitle(title)
            .setMessage(description)
            .setPositiveButton(settings) { _, _ ->
                it.execute(PermissionSettingsAction)
                onSettings()
            }
            .setNegativeButton(cancel) { _, _ -> onCancel() }
            .setCancelable(isCancelable)
            .show()
    })

}