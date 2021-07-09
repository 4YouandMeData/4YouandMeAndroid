package com.foryouandme.ui.compose.web

import android.view.ViewGroup
import android.webkit.WebView
import androidx.compose.foundation.layout.*
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.foryouandme.core.ext.web.setupWebViewWithCookies
import com.foryouandme.entity.configuration.Configuration

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
                    success = success,
                    failure = failure
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