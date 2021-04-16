package com.foryouandme.ui.aboutyou.dailysurveytime

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.foryouandme.R
import com.foryouandme.core.arch.flow.observeIn
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.ext.imageConfiguration
import com.foryouandme.core.ext.setStatusBar
import com.foryouandme.core.ext.showBackButton
import com.foryouandme.databinding.DailySurveyTimeBinding
import com.foryouandme.entity.configuration.button.button
import com.foryouandme.ui.aboutyou.AboutYouSectionFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import org.threeten.bp.LocalTime

@AndroidEntryPoint
class AboutYouDailySurveyTimeFragment :
    AboutYouSectionFragment(R.layout.daily_survey_time) {

    private val viewModel: AboutYouDailySurveyTimeViewModel by viewModels()

    private val binding: DailySurveyTimeBinding?
        get() = view?.let { DailySurveyTimeBinding.bind(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateUpdate
            .unwrapEvent(name)
            .onEach {
                when(it) {
                    AboutYouDailySurveyTimeStateUpdate.GetUserSettings -> applyTime()
                    AboutYouDailySurveyTimeStateUpdate.SaveUserSettings -> back()
                }
            }
            .observeIn(this)

        viewModel.loading
            .unwrapEvent(name)
            .onEach {
                when (it.task) {
                    AboutYouDailySurveyTimeLoading.GetUserSettings ->
                        binding?.loading?.setVisibility(it.active, false)
                    AboutYouDailySurveyTimeLoading.SaveUserSettings ->
                        binding?.loading?.setVisibility(it.active)
                }
            }
            .observeIn(this)

        viewModel.error
            .unwrapEvent(name)
            .onEach {
                when (it.cause) {
                    AboutYouDailySurveyTimeError.GetUserSettings ->
                        binding?.error?.setError(it.error, configuration) {
                            viewModel.execute(AboutYouDailySurveyTimeStateEvent.GetUserSettings)
                        }
                    AboutYouDailySurveyTimeError.SaveUserSettings ->
                        binding?.error?.setError(it.error, configuration)
                }
            }
            .observeIn(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        applyConfiguration()
        if (viewModel.state.userSettings == null)
            viewModel.execute(AboutYouDailySurveyTimeStateEvent.GetUserSettings)
        else
            applyTime()

    }

    override fun onConfigurationChange() {
        super.onConfigurationChange()
        applyConfiguration()
    }

    private fun applyConfiguration() {

        val viewBinding = binding
        val config = configuration

        if (viewBinding != null && config != null) {

            setStatusBar(config.theme.primaryColorStart.color())

            viewBinding.root.setBackgroundColor(config.theme.secondaryColor.color())

            viewBinding.toolbar.setBackgroundColor(config.theme.primaryColorStart.color())
            viewBinding.toolbar.showBackButton(imageConfiguration) {
                aboutYouViewModel.back(aboutYouNavController(), rootNavController())
            }

            viewBinding.title.setTextColor(config.theme.secondaryColor.color())
            viewBinding.title.text = config.text.profile.fifthItem

            viewBinding.description.setTextColor(config.theme.primaryTextColor.color())
            viewBinding.description.text = config.text.profile.dailySurveyTimeDescription

            viewBinding.timePicker.setIs24HourView(false)

            // TODO: move to configuration
            viewBinding.saveButton.text = "Save"
            viewBinding.saveButton.setTextColor(config.theme.secondaryTextColor.color())
            viewBinding.saveButton.background = button(config.theme.primaryColorStart.color())
            viewBinding.saveButton.setOnClickListener {

                val timePicker = binding?.timePicker

                if (timePicker != null) {

                    val localTime = LocalTime.of(timePicker.hour, timePicker.minute)
                    viewModel.execute(AboutYouDailySurveyTimeStateEvent.SaveUserSettings(localTime))

                }

            }
        }
    }

    private fun applyTime() {

        val time = viewModel.state.userSettings?.dailySurveyTime
        val viewBinding = binding

        if (viewBinding != null && time != null) {
            viewBinding.timePicker.hour = time.hour
            viewBinding.timePicker.minute = time.minute
        }

    }

}