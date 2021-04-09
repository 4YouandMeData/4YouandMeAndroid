package com.foryouandme.researchkit.step.nineholepeg

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import com.foryouandme.researchkit.step.StepFragment

class NineHolePegFragment : StepFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
            setContent {
                NineHolePegPoint()
            }
        }

}