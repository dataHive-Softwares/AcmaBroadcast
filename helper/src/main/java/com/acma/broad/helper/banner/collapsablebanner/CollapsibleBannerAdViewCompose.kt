package com.acma.broad.helper.banner.collapsablebanner

import android.app.Activity
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.viewinterop.AndroidView
import com.acma.broad.helper.core.AdsConfig
import com.acma.broad.helper.extension.findActivity
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun CollapsibleBannerAdView() {

    val shouldShowAds = AdsConfig.shouldEnableCollapsibleBannerAds().collectAsState()
    if (shouldShowAds.value.not()) return

    val context = LocalContext.current

    // Skip in preview mode to avoid crashes
    if (LocalInspectionMode.current) return

    // Resolve Activity from context for adaptive sizing
    val activity = context.findActivity() ?: return

    // Create and remember AdView
    val adView = remember {
        AdView(context).apply {
            adUnitId = AdsConfig.COLLAPSIBLE_BANNER_ID
            setAdSize(activity.getAdaptiveBannerAdSize())

            // Pass AdMob-specific extras for collapsible behavior
            val extras = Bundle().apply {
                putString("collapsible", "bottom")
            }

            val adRequest = AdRequest.Builder()
                .addNetworkExtrasBundle(AdMobAdapter::class.java, extras)
                .build()

            loadAd(adRequest)
        }
    }

    // Compose wrapper for AdView
    AndroidView(
        factory = { adView }
    )

    // Clean up the ad view properly
    DisposableEffect(adView) {
        onDispose {
            adView.destroy()
        }
    }
}

/**
 * Calculate the adaptive banner size based on the Activity's current screen width.
 */
private fun Activity.getAdaptiveBannerAdSize(): AdSize {
    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)

    val density = displayMetrics.density
    val adWidthPixels = displayMetrics.widthPixels.toFloat()
    val adWidthDp = (adWidthPixels / density).toInt()

    return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidthDp)
}
