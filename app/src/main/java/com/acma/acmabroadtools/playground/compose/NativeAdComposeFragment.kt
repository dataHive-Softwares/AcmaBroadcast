package com.acma.acmabroadtools.playground.compose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.acma.broad.helper.nativead.compose.NativeAdSize
import com.acma.broad.helper.nativead.compose.NativeAdViewCompose

class NativeAdComposeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                NativeAdViewCompose(NativeAdSize.LARGE)
                Spacer(modifier = Modifier.height(10.dp))

                NativeAdViewCompose(NativeAdSize.MEDIUM)
                Spacer(modifier = Modifier.height(10.dp))

            }
        }
    }
}