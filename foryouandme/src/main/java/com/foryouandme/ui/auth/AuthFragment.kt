package com.foryouandme.ui.auth

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import com.foryouandme.R
import com.foryouandme.core.arch.android.BaseFragment
import com.foryouandme.core.ext.catchToNull
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthFragment : BaseFragment(R.layout.auth) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigation()
    }

    private fun setupNavigation() {

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val currentGraph = catchToNull { navHostFragment.navController.graph }
        if (currentGraph == null) {
            val inflater = navHostFragment.navController.navInflater
            val graph = inflater.inflate(R.navigation.auth_navigation)
            graph.setStartDestination(if (updateConsentArg()) R.id.onboarding else R.id.splash)
            navHostFragment.navController.graph = graph
        }

    }

    fun updateConsentArg(): Boolean = arguments?.getBoolean(UPDATE_CONSENT, false) ?: false

    companion object {

        private const val UPDATE_CONSENT = "update_consent"

        fun build(updateConsent: Boolean): Bundle =
            Bundle().apply { putBoolean(UPDATE_CONSENT, updateConsent) }

    }

}