package com.acma.broad.helper.nativead.nativelist

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.acma.broad.helper.nativead.GenericNativeAdView

/**
 *
 * A ViewHolder that holds and manages a [GenericNativeAdView] for displaying
 * native ads inside a RecyclerView.
 *
 * @param itemView The view representing the native ad layout. Expected to be an instance of [GenericNativeAdView].
 */
class NativeAdViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    // Cast the itemView to genericNativeAdView since this ViewHolder is specifically for ads
    private val genericNativeAdView: GenericNativeAdView = itemView as GenericNativeAdView

    /**
     * Loads a native ad into the [GenericNativeAdView].
     * This method should be called when binding this ViewHolder.
     */
    fun loadAd() {
        genericNativeAdView.loadAdForListContent()
    }
}
