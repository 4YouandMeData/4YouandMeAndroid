package com.foryouandme.ui.aboutyou.userInfo

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.foryouandme.R
import com.foryouandme.ui.aboutyou.AboutYouSectionFragment
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.core.entity.configuration.Configuration
import com.foryouandme.core.ext.*
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.adapter.DroidAdapter
import com.giacomoparisi.recyclerdroid.core.decoration.LinearMarginItemDecoration
import kotlinx.android.synthetic.main.user_info.*

class AboutYouUserInfoFragment :
    AboutYouSectionFragment<AboutYouUserInfoViewModel>(R.layout.user_info) {
    override val viewModel: AboutYouUserInfoViewModel by lazy {

        viewModelFactory(
            this,
            getFactory {
                AboutYouUserInfoViewModel(
                    navigator,
                    injector.authModule()
                )
            }
        )

    }

    private val adapter: DroidAdapter by lazy {

        DroidAdapter(
            EntryStringViewHolder.factory { item, text ->
                startCoroutineAsync { viewModel.updateTextItem(item.id, text) }
            },
            EntryDateViewHolder.factory { item, date ->
                startCoroutineAsync { viewModel.updateDateItem(item.id, date) }
            },
            EntryPickerViewHolder.factory { item, value ->
                startCoroutineAsync { viewModel.updatePickerItem(item.id, value) }
            }
        )

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateLiveData()
            .observeEvent(name()) { update ->
                when (update) {
                    is AboutYouUserInfoStateUpdate.EditMode ->
                        configuration {
                            bindEditMode(it, update.isEditing)
                            applyItems(update.items)
                        }
                    is AboutYouUserInfoStateUpdate.Items ->
                        startCoroutineAsync { applyItems(update.items) }
                }
            }

        viewModel.loadingLiveData()
            .observeEvent(name()) {
                when (it.task) {
                    AboutYouUserInfoLoading.Upload ->
                        loading.setVisibility(it.active, true)
                }
            }

        viewModel.errorLiveData()
            .observeEvent(name()) {
                when (it.cause) {
                    AboutYouUserInfoError.Upload -> errorToast(it.error)
                }
            }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        refreshUserAndConfiguration { config, user ->

            setupRecyclerView()
            applyConfiguration(config)

            if (viewModel.isInitialized().not())
                viewModel.initialize(config, imageConfiguration, user)

            applyItems(viewModel.state().items)
            bindEditMode(config, viewModel.state().isEditing)

        }
    }

    private suspend fun applyConfiguration(configuration: Configuration): Unit =
        evalOnMain {

            setStatusBar(configuration.theme.primaryColorStart.color())

            root.setBackgroundColor(configuration.theme.secondaryColor.color())

            toolbar.setBackgroundColor(configuration.theme.primaryColorStart.color())

            header.setBackgroundColor(configuration.theme.primaryColorStart.color())

            imageView.setImageResource(imageConfiguration.logoStudySecondary())

            toolbar.showBackButton(imageConfiguration) {

                startCoroutineAsync {
                    aboutYouViewModel.back(aboutYouNavController(), rootNavController())
                }

            }

            title.setTextColor(configuration.theme.secondaryColor.color())
            title.text = configuration.text.profile.firstItem

            edit_save_button.setImageResource(imageConfiguration.editContainer())
            edit_save_button.setOnClickListenerAsync { viewModel.toggleEdit(rootNavController()) }

            pencil.setImageResource(imageConfiguration.pencil())

            edit_save_text.setTextColor(configuration.theme.secondaryTextColor.color())

        }

    private suspend fun setupRecyclerView(): Unit =
        evalOnMain {

            recycler_view.layoutManager =
                LinearLayoutManager(
                    requireContext(),
                    RecyclerView.VERTICAL,
                    false
                )

            recycler_view.adapter = adapter

            recycler_view.addItemDecoration(
                LinearMarginItemDecoration(
                    topMargin = { it.isFirst(20.dpToPx(), 50.dpToPx()) },
                    startMargin = { 20.dpToPx() },
                    endMargin = { 20.dpToPx() },
                    bottomMargin = { it.isLast(40.dpToPx(), 0) }
                )
            )


        }

    private suspend fun applyItems(items: List<DroidItem<Any>>): Unit =
        evalOnMain {

            adapter.submitList(items)

        }

    private suspend fun bindEditMode(
        configuration: Configuration,
        isEditing: Boolean
    ): Unit =
        evalOnMain {

            pencil.isVisible = isEditing.not()

            edit_save_text.text =
                if (isEditing) configuration.text.profile.submit
                else configuration.text.profile.edit


        }
}