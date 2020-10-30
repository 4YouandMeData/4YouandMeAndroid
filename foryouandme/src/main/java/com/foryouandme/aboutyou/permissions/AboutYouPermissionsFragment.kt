package com.foryouandme.aboutyou.permissions

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.foryouandme.R
import com.foryouandme.aboutyou.AboutYouSectionFragment
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.core.entity.configuration.Configuration
import com.foryouandme.core.ext.*
import com.giacomoparisi.recyclerdroid.core.DroidAdapter
import com.giacomoparisi.recyclerdroid.core.decoration.LinearMarginItemDecoration
import kotlinx.android.synthetic.main.permissions.*

class AboutYouPermissionsFragment :
    AboutYouSectionFragment<AboutYouPermissionsViewModel>(R.layout.permissions) {
    override val viewModel: AboutYouPermissionsViewModel by lazy {

        viewModelFactory(
            this,
            getFactory { AboutYouPermissionsViewModel(navigator, injector.permissionModule()) }
        )

    }

    private val adapter: DroidAdapter by lazy {
        DroidAdapter(PermissionsViewHolder.factory { item ->

            if (item.isAllowed.not())
                configuration { viewModel.requestPermission(item, it, imageConfiguration) }

        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateLiveData()
            .observeEvent(name()) {
                when (it) {
                    is AboutYouPermissionsStateUpdate.Initialization ->
                        startCoroutineAsync { applyPermissions(it.permissions) }
                }
            }

        viewModel.loadingLiveData()
            .observeEvent(name()) {
                when (it.task) {
                    AboutYouPermissionsLoading.Initialization ->
                        loading.setVisibility(it.active, true)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configuration {
            setupRecyclerView()
            applyConfiguration(it)

            if (viewModel.isInitialized().not())
                viewModel.initialize(it, imageConfiguration)
            else
                applyPermissions(viewModel.state().permissions)

        }
    }

    private suspend fun applyConfiguration(configuration: Configuration): Unit =
        evalOnMain {

            setStatusBar(configuration.theme.primaryColorStart.color())

            root.setBackgroundColor(configuration.theme.secondaryColor.color())

            toolbar.setBackgroundColor(configuration.theme.primaryColorStart.color())
            toolbar.showBackButtonSuspend(imageConfiguration) {
                aboutYouViewModel.back(aboutYouNavController(), rootNavController())
            }

            title.setTextColor(configuration.theme.secondaryColor.color())
            title.text = configuration.text.profile.fourthItem

        }

    private suspend fun setupRecyclerView(): Unit =
        evalOnMain {

            recycler_view.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

            recycler_view.adapter = adapter

            recycler_view.addItemDecoration(
                LinearMarginItemDecoration(
                    {
                        if (it.index == 0) 50.dpToPx()
                        else 30.dpToPx()
                    },
                    { 20.dpToPx() },
                    { 20.dpToPx() },
                    {
                        if (it.index == it.itemCount) 30.dpToPx()
                        else 0.dpToPx()
                    }
                )
            )

        }

    private suspend fun applyPermissions(permissions: List<PermissionsItem>): Unit =
        evalOnMain {

            adapter.submitList(permissions)

        }
}