package com.foryouandme.ui.htmldetails

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.foryouandme.R
import com.foryouandme.core.arch.android.BaseFragment
import com.foryouandme.core.arch.flow.observeIn
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.ext.setStatusBar
import com.foryouandme.core.ext.showBackButton
import com.foryouandme.databinding.HtmlDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class HtmlDetailsFragment : BaseFragment(R.layout.html_detail) {

    private val args: HtmlDetailsFragmentArgs by navArgs()

    private val viewModel: HtmlDetailsViewModel by viewModels()

    private val binding: HtmlDetailBinding?
        get() = view?.let { HtmlDetailBinding.bind(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateUpdate
            .unwrapEvent(name)
            .onEach {
                when (it) {
                    is HtmlDetailsStateUpdate.Initialization -> {
                        applyConfiguration()
                        setupWebView(args.pageId)
                    }
                }
            }
            .observeIn(this)

        viewModel.error
            .unwrapEvent(name)
            .onEach {
                when (it.cause) {
                    HtmlDetailsError.Initialization -> {
                        binding?.loading?.setVisibility(false)
                        binding?.error?.setError(it.error, null)
                        { viewModel.execute(HtmlDetailsStateEvent.Initialize) }
                    }
                }
            }
            .observeIn(this)

        viewModel.loading
            .unwrapEvent(name)
            .onEach {
                when (it.task) {
                    HtmlDetailsLoading.Initialization ->
                        binding?.loading?.setVisibility(it.active, false)
                }
            }
            .observeIn(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (viewModel.state.configuration == null && viewModel.state.studyInfo == null)
            viewModel.execute(HtmlDetailsStateEvent.Initialize)
        else {
            applyConfiguration()
        }

    }

    private fun applyConfiguration() {

        val viewBinding = binding
        val configuration = viewModel.state.configuration

        if (viewBinding != null && configuration != null) {

            setStatusBar(configuration.theme.primaryColorStart.color())

            viewBinding.root.setBackgroundColor(configuration.theme.secondaryColor.color())

            viewBinding.toolbar.setBackgroundColor(configuration.theme.primaryColorStart.color())
            viewBinding.toolbar.showBackButton(imageConfiguration) {
                navigator.back(rootNavController())
            }

            viewBinding.title.setTextColor(configuration.theme.secondaryColor.color())

        }

    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView(page: EHtmlDetails) {

        val viewBinding = binding
        val studyInfo = viewModel.state.studyInfo

        if (viewBinding != null && studyInfo != null) {

            var source = ""
            when (page) {
                EHtmlDetails.INFO -> {
                    viewBinding.title.text = studyInfo.informationPage.title
                    source = studyInfo.informationPage.body
                }
                EHtmlDetails.REWARD -> {
                    viewBinding.title.text = studyInfo.rewardPage.title
                    source = studyInfo.rewardPage.body
                }
                EHtmlDetails.FAQ -> {
                    viewBinding.title.text = studyInfo.faqPage.title
                    source = studyInfo.faqPage.body
                }
            }

            viewBinding.webView.settings.also {
                it.javaScriptEnabled = true
            }

            viewBinding.webView.loadDataWithBaseURL(
                null,
                source,
                "text/html; charset=utf-8",
                "utf-8",
                null
            )
        }
    }

}