package com.acma.broad.helper.nativead.compose

import android.graphics.Color
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.acma.broad.helper.core.AdsConfig
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView

/**
 * A Jetpack Compose Composable that displays a medium-sized Google Native Ad
 * using the [NativeAdView] inside a Compose UI via [AndroidView].
 *
 * This layout includes:
 * - Icon (ImageView)
 * - Headline (TextView)
 * - Advertiser (TextView)
 * - Body (TextView)
 * - Call-to-action button (Button)
 *
 * Layout structure:
 * - Horizontal layout with:
 *    - Icon on the left
 *    - Vertical stack of texts and CTA on the right
 *
 * @param nativeAd The loaded [NativeAd] object provided by AdMob containing all ad content.
 * @param modifier Optional [Modifier] for layout customization in Jetpack Compose.
 *
 * Example usage:
 * ```
 * MediumNativeAdView(
 *     nativeAd = myNativeAd,
 *     modifier = Modifier
 *         .fillMaxWidth()
 *         .padding(8.dp)
 * )
 * ```
 *
 * Note: This should be used only after the [NativeAd] is loaded. Always manage ad lifecycle properly to avoid memory leaks.
 */

@Composable
internal fun MediumNativeAdView(nativeAd: NativeAd, modifier: Modifier = Modifier) {

    val shouldShowAds =  AdsConfig.shouldEnableNativeAds().value
    if (shouldShowAds?.not() ?: true) return

    val context = LocalContext.current

    AndroidView(
        factory = {
            // Root view provided by AdMob to hold ad assets
            val adView = NativeAdView(context)

            // Icon ImageView
            val iconView = ImageView(context).apply {
                id = View.generateViewId()
                nativeAd.icon?.drawable?.let { setImageDrawable(it) }
                layoutParams = LinearLayout.LayoutParams(100, 100).apply {
                    setMargins(0, 0, 16, 0)
                }
            }

            // Headline TextView (required)
            val headlineView = TextView(context).apply {
                id = View.generateViewId()
                text = nativeAd.headline
                textSize = 18f
                setTextColor(Color.BLACK)
            }

            // Advertiser TextView (optional)
            val advertiserView = TextView(context).apply {
                id = View.generateViewId()
                text = nativeAd.advertiser
                textSize = 14f
                setTextColor(Color.DKGRAY)
            }

            // Body TextView (optional)
            val bodyView = TextView(context).apply {
                id = View.generateViewId()
                text = nativeAd.body
                textSize = 14f
                setTextColor(Color.DKGRAY)
            }

            // Call to Action Button (required if available)
            val callToActionView = Button(context).apply {
                id = View.generateViewId()
                text = nativeAd.callToAction
            }

            // Vertical layout for textual content and button
            val verticalLayout = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(16, 16, 16, 16)
                setBackgroundColor(Color.WHITE)

                addView(headlineView)
                addView(advertiserView)
                addView(bodyView)
                addView(callToActionView)
            }

            // Horizontal layout with icon and content
            val horizontalLayout = LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                addView(iconView)
                addView(verticalLayout, LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ))
            }

            // Add the entire layout to the ad view
            adView.addView(horizontalLayout)

            // Register ad views with NativeAdView for tracking
            adView.headlineView = headlineView
            adView.iconView = iconView
            adView.advertiserView = advertiserView
            adView.bodyView = bodyView
            adView.callToActionView = callToActionView

            // Bind the ad data to the view (must be last)
            adView.setNativeAd(nativeAd)

            adView
        },
        modifier = modifier
    )
}
