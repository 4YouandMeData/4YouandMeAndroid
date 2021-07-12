package com.foryouandme.ui.compose.web

import android.view.ViewGroup
import android.webkit.WebView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.core.ext.web.setupWebViewWithCookies
import com.foryouandme.entity.configuration.Configuration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun Web(
    url: String,
    configuration: Configuration,
    showProgress: Boolean,
    modifier: Modifier = Modifier,
    cookies: Map<String, String> = emptyMap(),
    success: () -> Unit = {},
    failure: () -> Unit = {},
) {

    var progress by remember { mutableStateOf(0) }
    val coroutineScope = rememberCoroutineScope()

    var view by remember { mutableStateOf<WebView?>(null) }

    DisposableEffect("web") {
        onDispose {

            view?.destroy()
            view = null

        }
    }

    Box(modifier) {
        AndroidView(
            factory = { context ->
                val webView = WebView(context)
                webView.layoutParams =
                    ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                webView.setupWebViewWithCookies(
                    url = url,
                    onProgressChanged = { progress = it },
                    cookies = cookies,
                    success = {
                        coroutineScope.launchSafe { withContext(Dispatchers.Main) { success() } }
                    },
                    failure = {
                        coroutineScope.launchSafe { withContext(Dispatchers.Main) { failure() } }
                    }
                )
                view = webView
                webView
            },
            modifier = Modifier.fillMaxSize()
        )
        if (showProgress && progress < 100)
            LinearProgressIndicator(
                progress = progress / 100f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp),
                color = configuration.theme.activeColor.value,
                backgroundColor = configuration.theme.activeColor.value.copy(alpha = 0.5f)
            )
    }
}