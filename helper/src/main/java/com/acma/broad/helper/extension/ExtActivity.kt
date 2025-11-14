package com.acma.broad.helper.extension

import android.app.Activity
import android.util.DisplayMetrics
import com.google.android.gms.ads.AdSize


/**
 * Calculate the adaptive banner size based on the Activity's current screen width.
 */
internal fun Activity.getAdaptiveBannerAdSize(): AdSize {
    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)

    val density = displayMetrics.density
    val adWidthPixels = displayMetrics.widthPixels.toFloat()
    val adWidthDp = (adWidthPixels / density).toInt()

    return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidthDp)
}