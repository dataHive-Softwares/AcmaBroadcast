package com.acma.broad.helper.nativead.compose

import android.R
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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
internal fun LargeNativeAdView(
    nativeAd: NativeAd,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    AndroidView(
        factory = {
            val adView = NativeAdView(context)

            // Media (video/image)
            val mediaView = MediaView(context).apply {
                id = View.generateViewId()
            }

            // Icon
            val iconView = ImageView(context).apply {
                id = View.generateViewId()
                layoutParams = LinearLayout.LayoutParams(100, 100)
                nativeAd.icon?.drawable?.let { setImageDrawable(it) }
                scaleType = ImageView.ScaleType.CENTER_CROP
                clipToOutline = true
                background = context.getDrawable(android.R.drawable.picture_frame)
            }

            // Headline
            val headlineView = TextView(context).apply {
                id = View.generateViewId()
                text = nativeAd.headline
                textSize = 18f
                setTypeface(typeface, Typeface.BOLD)
                setTextColor(Color.BLACK)
            }

            // Rating (optional)
            val starRating = RatingBar(context, null, R.attr.ratingBarStyleSmall).apply {
                id = View.generateViewId()
                numStars = 5
                stepSize = 0.5f
                rating = nativeAd.starRating?.toFloat() ?: 0f
                visibility = if (nativeAd.starRating != null) View.VISIBLE else View.GONE
            }

            // Body
            val bodyView = TextView(context).apply {
                id = View.generateViewId()
                text = nativeAd.body ?: ""
                setTextColor(Color.DKGRAY)
                textSize = 14f
                maxLines = 2
            }

            // CTA
            val callToActionView = Button(context).apply {
                id = View.generateViewId()
                text = nativeAd.callToAction
                textSize = 15f
                setTypeface(typeface, Typeface.BOLD)
                setPadding(24, 12, 24, 12)
                background = GradientDrawable().apply {
                    cornerRadius = 50f
                    setColor(Color.parseColor("#1A73E8")) // Google blue
                }
                setTextColor(Color.WHITE)
            }

            // Top layout (icon + headline + rating)
            val headerLayout = LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
                setPadding(0, 0, 0, 8)

                addView(iconView)
                addView(LinearLayout(context).apply {
                    orientation = LinearLayout.VERTICAL
                    setPadding(16, 0, 0, 0)
                    addView(headlineView)
                    addView(starRating)
                })
            }

            // Parent layout
            val layout = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(20, 20, 20, 20)
                setBackgroundColor(Color.WHITE)
                background = GradientDrawable().apply {
                    cornerRadius = 30f
                    setColor(Color.WHITE)
                }
                elevation = 10f

                addView(mediaView, LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    350
                ))
                addView(headerLayout)
                addView(bodyView)
                addView(callToActionView.apply {
                    val params = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    params.gravity = Gravity.END
                    params.topMargin = 16
                    layoutParams = params
                })
            }

            // Bind
            adView.mediaView = mediaView
            adView.headlineView = headlineView
            adView.iconView = iconView
            adView.bodyView = bodyView
            adView.callToActionView = callToActionView
            adView.starRatingView = starRating

            adView.addView(layout)
            adView.setNativeAd(nativeAd)

            adView
        },
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp)
    )
}

