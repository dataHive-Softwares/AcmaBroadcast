package com.example.sdkads.nativead

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView


/**
 * A Jetpack Compose Composable that displays a large native ad
 * using Google's NativeAdView inside a Compose UI with AndroidView interop.
 *
 * This function builds a basic layout for a native ad that includes:
 * - Headline (TextView)
 * - Media (MediaView for video/image)
 * - Call-to-action button
 *
 * The layout is built using traditional Android Views and is rendered inside Compose using AndroidView.
 *
 * @param nativeAd The [NativeAd] object containing all ad data to be rendered.
 * @param modifier Optional [Modifier] to control layout behavior in Compose (e.g., padding, width).
 *
 * Example usage:
 * ```
 * LargeNativeAdView(
 *     nativeAd = myLoadedNativeAd,
 *     modifier = Modifier
 *         .fillMaxWidth()
 *         .padding(8.dp)
 * )
 * ```
 *
 * Make sure you call this only after the [NativeAd] has been successfully loaded.
 */

@Composable
fun LargeNativeAdView(nativeAd: NativeAd, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    AndroidView(
        factory = {
            // Create root view required by Google Ads SDK
            val adView = NativeAdView(context)

            // Create a MediaView to display video or image content
            val mediaView = MediaView(context).apply {
                id = View.generateViewId()
            }

            // Create a TextView to display the ad headline
            val headlineView = TextView(context).apply {
                id = View.generateViewId()
                text = nativeAd.headline
                textSize = 18f
                setTextColor(android.graphics.Color.BLACK)
            }

            // Create a CTA (Call to Action) button
            val callToActionView = android.widget.Button(context).apply {
                id = View.generateViewId()
                text = nativeAd.callToAction
            }

            // Create a vertical layout to arrange views
            val layout = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(16, 16, 16, 16)
                setBackgroundColor(android.graphics.Color.WHITE)

                addView(headlineView)
                addView(mediaView, LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    300
                ))
                addView(callToActionView)
            }

            // Register ad assets with the NativeAdView for tracking
            adView.headlineView = headlineView
            adView.mediaView = mediaView
            adView.callToActionView = callToActionView

            // Attach the layout to the NativeAdView
            adView.addView(layout)

            // Must be called last to bind ad data to the view
            adView.setNativeAd(nativeAd)

            adView
        },
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    )
}

