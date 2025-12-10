package com.acma.broad.helper.core

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.ads.AdRequest
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Configuration object for managing ad settings across the application.
 * Dynamically assigns AdMob ad unit IDs based on the build variant.
 */
object AdsConfig {
    /**
     * The AdMob ad unit ID for fixed-size banner ads.
     * Defaults to test ad unit ID in debug builds.
     */
    var BANNER_ID: String = "ca-app-pub-3940256099942544/6300978111"

    /**
     * The AdMob ad unit ID for interstitial ads.
     * Defaults to test ad unit ID in debug builds.
     */
    var INTERSTITIAL_AD_ID: String = "ca-app-pub-3940256099942544/1033173712"

    /**
     * The AdMob ad unit ID for adaptive banner ads.
     * Defaults to test ad unit ID in debug builds.
     */
    var ADAPTIVE_BANNER_ID: String = "ca-app-pub-3940256099942544/6300978111"

    /**
     * The AdMob ad unit ID for reward interstitial ads.
     * Defaults to test ad unit ID in debug builds.
     */
    var REWARD_INTERSTITIAL_AD_ID: String = "ca-app-pub-3940256099942544/5354046379"

    /**
     * The AdMob ad unit ID for reward ads.
     * Defaults to test ad unit ID in debug builds.
     */
    var REWARDED_AD_ID: String = "ca-app-pub-3940256099942544/5224354917"

    /**
     * The AdMob ad unit ID for native ads.
     * Defaults to test ad unit ID in debug builds.
     */
    var NATIVE_AD_ID: String = "ca-app-pub-3940256099942544/2247696110"

    /**
     * The AdMob ad unit ID for app open.
     * Defaults to test ad unit ID in debug builds.
     */
    var APP_OPEN_ID: String = "ca-app-pub-3940256099942544/3419835294"

    /**
     * The AdMob ad unit ID for collapsible banner.
     * Defaults to test ad unit ID in debug builds.
     */
    var COLLAPSIBLE_BANNER_ID: String = "ca-app-pub-3940256099942544/6300978111"

    /**
     * Is Debug check for showing consent
     */
    var IS_DEBUG:Boolean = false

    var HashedId: List<String> = emptyList()

    val request: AdRequest
        get() = AdRequest.Builder().build()

    fun shouldEnableNativeAds() : MutableLiveData<Boolean> = MutableLiveData( true)
    fun shouldEnableInterstitialAds() : MutableStateFlow<Boolean> = MutableStateFlow(value = true)
    fun shouldEnableAppOpenAds() : MutableStateFlow<Boolean> = MutableStateFlow(value = true)
    fun shouldEnableRewardAds() : MutableStateFlow<Boolean> = MutableStateFlow(value = true)
    fun shouldEnableAdaptiveBannerAds() : MutableStateFlow<Boolean> = MutableStateFlow(value = true)
    fun shouldEnableCollapsibleBannerAds() : MutableStateFlow<Boolean> = MutableStateFlow(value = true)


}