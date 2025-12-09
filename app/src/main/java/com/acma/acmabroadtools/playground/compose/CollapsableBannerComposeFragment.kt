package com.acma.acmabroadtools.playground.compose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.acma.broad.helper.banner.collapsablebanner.CollapsibleBannerAdView

class CollapsableBannerComposeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                CollapsibleBannerAdView()
            }
        }
    }
}