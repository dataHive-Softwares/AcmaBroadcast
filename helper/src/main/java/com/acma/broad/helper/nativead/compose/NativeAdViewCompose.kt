package com.acma.broad.helper.nativead.compose

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.acma.broad.helper.core.AdsConfig
import com.acma.broad.helper.nativead.compose.NativeAdSize.LARGE
import com.acma.broad.helper.nativead.compose.NativeAdSize.MEDIUM
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions


/**
 * Enum class representing different layout sizes for native ads.
 * - [MEDIUM]: Shows the ad using a compact layout.
 * - [LARGE]: Shows the ad using a larger layout (e.g., more padding, additional assets).
 */

enum class NativeAdSize {
    MEDIUM, LARGE
}

/**
 * Composable function that loads and displays a Google Native Ad based on the given size.
 * It uses Jetpack Compose's [DisposableEffect] to manage ad lifecycle, ensuring proper
 * cleanup to avoid memory leaks.
 *
 * ## Features:
 * - Automatically loads a native ad on composition
 * - Supports two ad sizes: MEDIUM and LARGE
 * - Uses appropriate native ad Composables ([MediumNativeAdView] or [LargeNativeAdView])
 * - Cleans up old ads and destroys them properly when no longer used
 *
 * @param adSize Determines the layout style for the native ad. Defaults to [NativeAdSize.LARGE].
 *
 * ## Usage:
 * ```kotlin
 * NativeAdViewCompose(adSize = NativeAdSize.MEDIUM)
 * ```
 *
 * ## Notes:
 * - Requires internet permission and proper AdMob setup.
 * - Make sure [AdsConfig.NATIVE_AD_ID] contains a valid ad unit ID.
 */

@Composable
fun NativeAdViewCompose(
    adSize: NativeAdSize = NativeAdSize.LARGE
) {

    val shouldShowAds =  AdsConfig.shouldEnableNativeAds().value
    if (shouldShowAds?.not() ?: true) return

    val context = LocalContext.current
    // State to hold the loaded native ad
    var nativeAd by remember { mutableStateOf<NativeAd?>(null) }

    /**
     * DisposableEffect is used to manage the side-effect of loading and destroying the ad.
     * This ensures that when the Composable leaves the composition, the ad is properly released.
     */
    DisposableEffect(Unit) {
        // Create AdLoader for loading native ads
        val adLoader = AdLoader.Builder(context, AdsConfig.NATIVE_AD_ID)
            .forNativeAd { ad ->
                // Destroy previously loaded ad to prevent memory leaks
                nativeAd?.destroy() // Clean up any previously loaded ad
                nativeAd = ad
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.e("NativeAd", "Failed to load native ad: ${adError.message}")
                }
            })
            .withNativeAdOptions(
                NativeAdOptions.Builder()
                    .setRequestMultipleImages(false)
                    .build()
            )
            .build()

        // Load the ad
        adLoader.loadAd(AdRequest.Builder().build())

        // Dispose action to clean up when the Composable is removed
        onDispose {
            nativeAd?.destroy()
            nativeAd = null
        }
    }

    /**
     * Once the ad is successfully loaded, display the appropriate native ad layout
     * based on the provided ad size.
     */
    nativeAd?.let {
        when (adSize) {
            NativeAdSize.LARGE -> LargeNativeAdView(nativeAd = it)
            NativeAdSize.MEDIUM -> MediumNativeAdView(nativeAd = it)
        }
    }
}
