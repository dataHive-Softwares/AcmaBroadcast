package com.acma.broad.helper.nativead

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.withStyledAttributes
import androidx.lifecycle.Observer
import com.acma.broad.helper.R
import com.acma.broad.helper.core.AdsConfig
import com.acma.broad.helper.extension.layoutInflater
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView

class GenericNativeAdView @JvmOverloads constructor(
    private val mContext: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(mContext, attrs, defStyleAttr) {

    private val shouldEnableNativeAds = AdsConfig.shouldEnablenavtiveAds()
    private val contentDefault = 0
    private val contentSmall = 1
    private val contentMedium = 2
    private val contentLarge = 3
    private val nativeDefault = 0
    private val root = this
    private var nativeContentViewType = contentDefault
    private var nativeAdViewType = nativeDefault
    private var nativeAdView: NativeAdView? = null

    /**
     * load ad with native ad id
     */
    init {
        loadAttrs(attrs)
        if (isInEditMode) {
            // add content layout to GenericNativeAdView for preview
            layoutInflater().inflate(getContentLayoutId(), root, true)
        }
    }

    fun loadAdForListContent() {
        if (shouldEnableNativeAds.value == true) {
            showAd()
        } else {
            hideAd()
        }
    }

    private var isAdAlreadyLoaded = false

    private val observer = Observer { enabled: Boolean ->
        Log.d("Ads", "Ads are enabled : $enabled")
        if (enabled && (java.lang.Boolean.TRUE == shouldEnableNativeAds.value)) {
            showAd()
        } else {
            hideAd()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        shouldEnableNativeAds.observeForever(observer)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        shouldEnableNativeAds.removeObserver(observer)
    }

    private val adLoader: AdLoader by lazy {
        buildAdLoader()
    }

    private fun showAd() {
        if (adLoader.isLoading)
            return
        visibility = VISIBLE
        if (isAdAlreadyLoaded) {
            Log.d("Ads", "Generic native ad is already loaded")
            val nativeAdViewRoot = nativeAdViewRoot
            if (nativeAdViewRoot != null && nativeAdViewRoot.isAttachedToWindow.not()) {
                root.addView(nativeAdViewRoot)
            }
        } else {
            nativeAdView = inflateViews()
            adLoader.loadAd(AdsConfig.request)
        }
    }

    private fun hideAd() {
        visibility = GONE
        removeAllViews()
        nativeAdView?.removeAllViews()
        nativeAdView?.destroy()
        isAdAlreadyLoaded = false
        nativeAdView = null
    }

    private fun loadAttrs(attrs: AttributeSet? = null) {
        mContext.withStyledAttributes(attrs, R.styleable.GenericNativeAdView, 0, 0) {

            if (hasValue(R.styleable.GenericNativeAdView_native_content_type)) {
                nativeContentViewType =
                    getInt(R.styleable.GenericNativeAdView_native_content_type, contentDefault)
            }

            if (hasValue(R.styleable.GenericNativeAdView_native_ad_type)) {
                nativeAdViewType =
                    getInt(R.styleable.GenericNativeAdView_native_ad_type, contentDefault)
            }
        }
    }

    private fun buildAdLoader() = getAdBuilder()
        .withNativeAdOptions(getAdOptions())
        .build()

    private fun getAdBuilder() = AdLoader.Builder(mContext, AdsConfig.NATIVE_AD_ID)
        .forNativeAd { nativeAd ->
            onNativeAdLoaded(nativeAd)
        }.withAdListener(object : AdListener() {
            override fun onAdFailedToLoad(p0: LoadAdError) {
//                findViewById<ShimmerFrameLayout>(R.id.shimmer)?.stopShimmer()
            }
        })

    private fun getAdOptions() = NativeAdOptions.Builder()
        .setVideoOptions(VideoOptions.Builder()
            .setStartMuted(true)
            .build())
        .build()

    private fun onNativeAdLoaded(nativeAd: NativeAd) {
        isAdAlreadyLoaded = true
//        val changeBounds = ChangeBounds()
//        changeBounds.duration = 500
//        TransitionManager.beginDelayedTransition(root.parent as ViewGroup, changeBounds)
        bindNativeView(nativeAd)
    }

    private fun inflateViews(): NativeAdView {
        inflateNativeAdView()
        val unifiedNativeAdView = findViewById<NativeAdView>(R.id.nativeView)
        // Inflate with shimmer layout
        layoutInflater().inflate(getContentShimmerLayoutId(), unifiedNativeAdView, true)
        return unifiedNativeAdView
    }

    private var nativeAdViewRoot: View? = null

    private fun inflateNativeAdView() {
        val adLayout =  R.layout.layout_generic_native_default_view
        layoutInflater().inflate(adLayout, root, true).also {
            nativeAdViewRoot = it
        }
    }

    private fun getContentLayoutId() = when (nativeContentViewType) {
        contentSmall -> R.layout.layout_generic_small_content
        contentMedium -> R.layout.layout_generic_default_content
        contentLarge -> R.layout.layout_generic_large_content
        else -> R.layout.layout_generic_default_content
    }

    private fun getContentShimmerLayoutId() = when (nativeContentViewType) {
        contentSmall -> R.layout.shimmer_generic_small_placeholder
        contentMedium -> R.layout.shimmer_generic_default_placeholder
        contentLarge -> R.layout.shimmer_generic_large_placeholder
        else -> R.layout.shimmer_generic_default_placeholder
    }

    private fun bindNativeView(nativeAd: NativeAd) {

        nativeAdView?.let { nativeAdView ->
            nativeAdView.removeAllViews()
            layoutInflater().inflate(getContentLayoutId(), nativeAdView, true)
        }

        nativeAdView?.apply {
            nativeAd.apply {
                // view initialization
                mediaView = findViewById(R.id.mediaView)
                headlineView = findViewById(R.id.headlineView)
                bodyView = findViewById(R.id.bodyView)
                callToActionView = findViewById(R.id.callToActionView)
                iconView = findViewById(R.id.iconView)
                val cvIcon = findViewById<View>(R.id.cvIcon)
                starRatingView = findViewById(R.id.starRatingView)
                storeView = findViewById(R.id.storeView)
                advertiserView = findViewById(R.id.advertiserView)

                // The headline is guaranteed to be in every UnifiedNativeAd.
                (headlineView as TextView?)?.text = headline

                if (mediaContent == null) {
                    mediaView?.visibility = INVISIBLE
                } else {
                    mediaView?.setMediaContent(mediaContent!!)
                    mediaView?.setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                }

                // set the body
                if (body == null) {
                    bodyView?.visibility = INVISIBLE
                } else {
                    bodyView?.visibility = VISIBLE
                    (bodyView as TextView?)?.text = body
                }

                // set call to action
                if (callToAction == null) {
                    callToActionView?.visibility = INVISIBLE
                } else {
                    callToActionView?.visibility = VISIBLE
                    (callToActionView as Button?)?.text = callToAction
                }

                // set icon
                if (icon == null) {
                    iconView?.visibility = INVISIBLE
                    cvIcon?.visibility = INVISIBLE
                } else {
                    iconView?.visibility = VISIBLE
                    (iconView as ImageView?)?.setImageDrawable(icon!!.drawable)
                }

                // set price
                if (price == null) {
                    priceView?.visibility = GONE
                } else {
                    priceView?.visibility = VISIBLE
                    (priceView as TextView?)?.text = price
                }

                if (store == null) {
                    storeView?.visibility = GONE
                } else {
                    storeView?.visibility = VISIBLE
                    (storeView as TextView?)?.text = store
                }

                if (starRating == null) {
                    starRatingView?.visibility = GONE
                } else {
                    starRatingView?.visibility = VISIBLE
                    (starRatingView as RatingBar?)?.rating = starRating!!.toFloat()
                }

                if (advertiser == null) {
                    advertiserView?.visibility = GONE
                } else {
                    advertiserView?.visibility = VISIBLE
                    (advertiserView as TextView?)?.text = advertiser
                }

                nativeAdView?.setNativeAd(this)
            }
        }
    }

}