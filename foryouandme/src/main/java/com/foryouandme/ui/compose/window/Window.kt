package com.foryouandme.ui.compose.window

import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import com.foryouandme.core.ext.findWindow

@Composable
fun KeepScreenOn(key: Any?) {

    val context = LocalContext.current

    DisposableEffect(key) {

        val window = context.findWindow()

        window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // When the effect leaves the Composition, remove the flags
        onDispose {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }

    }
}