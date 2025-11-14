package com.acma.broad.helper.banner.adaptivebanner

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.viewinterop.AndroidView
import com.acma.broad.helper.core.AdsConfig
import com.acma.broad.helper.extension.findActivity
import com.acma.broad.helper.extension.getAdaptiveBannerAdSize
import com.google.android.gms.ads.AdView

@Composable
fun AdaptiveBannerAdView(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    // Don't load ads in Preview Mode to avoid crashes
    if (LocalInspectionMode.current) {
        // Optionally show a placeholder in preview mode
        return
    }

    // Find the hosting Activity from Context, required for AdSize calculation
    val activity = context.findActivity() ?: return

    // Remember AdView instance, to keep it stable across recompositions
    val adView = remember {
        AdView(context).apply {
            adUnitId = AdsConfig.ADAPTIVE_BANNER_ID
            setAdSize(activity.getAdaptiveBannerAdSize())
        }
    }

    // Load ad when this composable enters composition or when adView changes
    LaunchedEffect(adView) {
        adView.loadAd(AdsConfig.request)
    }

    // Compose UI integration for the AdView
    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            // Create a NEW AdView for this composition
            AdView(ctx).apply {
                adUnitId = AdsConfig.ADAPTIVE_BANNER_ID
                setAdSize(activity.getAdaptiveBannerAdSize())
                loadAd(AdsConfig.request)
            } },
        update = {
            // If you need to refresh or update later, do it here
        }
    )

    // Clean up the AdView to prevent memory leaks when this composable leaves the composition
    DisposableEffect(adView) {
        onDispose {
            adView.destroy()
        }
    }
}



