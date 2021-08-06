package com.foryouandme.ui.studyinfo.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.foryouandme.core.arch.android.BaseFragment
import com.foryouandme.ui.studyinfo.detail.compose.StudyInfoDetailPage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StudyInfoDetailFragment : BaseFragment() {

    private val viewModel: StudyInfoDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
            setContent {
                StudyInfoDetailPage(
                    viewModel = viewModel,
                    onBack = { navigator.back(rootNavController()) }
                )
            }
        }

    /*private val args: com.foryouandme.ui.htmldetails.HtmlDetailsFragmentArgs by navArgs()

    private val viewModel: StudyInfoPageViewModel by viewModels()

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
    private fun setupWebView(htmlDetails: EHtmlDetails) {

        val viewBinding = binding
        val studyInfo = viewModel.state.studyInfo

        if (viewBinding != null && studyInfo != null) {

            var source = ""
            val page = when (htmlDetails) {
                EHtmlDetails.INFO -> studyInfo.informationPage
                EHtmlDetails.REWARD -> studyInfo.rewardPage
                EHtmlDetails.FAQ -> studyInfo.faqPage
            }

            if(page != null) {
                viewBinding.title.text = page.title
                source = page.body
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
    }*/

}