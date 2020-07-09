package org.fouryouandme.auth.wearable.login

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.*
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.web.*
import org.fouryouandme.R
import org.fouryouandme.auth.wearable.WearableViewModel
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.imageConfiguration
import org.fouryouandme.core.ext.navigator
import org.fouryouandme.core.ext.showCloseButton
import timber.log.Timber

class WearableLoginFragment : BaseFragment<WearableViewModel>(R.layout.web) {

    private val args: WearableLoginFragmentArgs by navArgs()

    override val viewModel: WearableViewModel by lazy {
        viewModelFactory(
            requireParentFragment(),
            getFactory { WearableViewModel(navigator, IORuntime) }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state().configuration
            .map { applyConfiguration(it) }

        toolbar.showCloseButton(imageConfiguration) {
            if (web_view.canGoBack())
                web_view.goBack()
            else
                viewModel.back(findNavController())
        }

        setupWebView()
    }

    private fun applyConfiguration(configuration: Configuration): Unit {

        toolbar.setBackgroundColor(Color.WHITE)

        progress_bar.progressTintList =
            ColorStateList.valueOf(configuration.theme.primaryColorStart.color())

        loadPage()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        web_view
            .also { it.settings.domStorageEnabled = true }
            .also { it.settings.javaScriptEnabled = true }
            .also { it.webViewClient = getWebClient() }
            .also { it.webChromeClient = getWebChromeClient() }
    }

    private fun loadPage(): Unit = web_view.loadUrl(args.url)

    private fun getWebClient(): WebViewClient =
        object : WebViewClient() {

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                web_view?.loadUrl(request?.url?.toString())
                return true
            }

            @SuppressLint("BinaryOperationInTimber")
            @TargetApi(Build.VERSION_CODES.M)
            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError
            ) {
                super.onReceivedError(view, request, error)
                Timber.e(
                    "${request?.url.toString()}: error:" +
                            " [${error.errorCode}] ${error.description}"
                )
            }

            override fun onReceivedError(
                view: WebView?,
                errorCode: Int,
                description: String?,
                failingUrl: String?
            ) {
                Timber.e("${failingUrl ?: ""}: error: $errorCode $description")
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }
        }

    private fun getWebChromeClient(): WebChromeClient =
        object : WebChromeClient() {

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)

                progress_bar.progress = newProgress
                progress_bar.isVisible = newProgress < 100
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()

        web_view.destroy()
    }
}