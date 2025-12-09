package com.acma.broad.helper

import android.app.Activity
import android.app.Application
import android.util.Log
import com.acma.broad.helper.appopen.AppOpenAdHelper
import com.acma.broad.helper.consentmanager.ConsentManager
import com.acma.broad.helper.core.AdsConfig
import com.acma.broad.helper.interstitial.InterstitialHelper
import com.acma.broad.helper.rewarded.RewardedAdHelper
import com.acma.broad.helper.rewarded.RewardedInterstitialHelper
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration

/**
 *
 * A singleton object responsible for initializing and managing the SDK Ads system.
 * It handles:
 * - Google Mobile Ads SDK initialization
 * - Ad unit configuration (banner, interstitial, native, etc.)
 * - App Open ad setup
 * - Interstitial ad preloading
 * - Consent form handling (via ConsentManager)
 */
object AdSdkInitializer{

    //    Manages App Open ads and excluded activity logic
    //    It’s not yet initialized (it’s null for now).
    //    It is meant to hold the instance of AppOpenAdHelper later.

    private var appOpenAdHelper: AppOpenAdHelper? = null

    // True/False switch to avoid loading the same full-screen ad multiple times.
    private var isInterstitialLoaded = false
    private var isRewardInterstitialLoaded = false
    private var isRewardLoaded = false

    /**
     * Initializes the Google Mobile Ads SDK, sets ad unit IDs, configures test devices, and
     * prepares interstitial and App Open ads.
     *
     * @param application The application instance required by MobileAds.
     * @param isDebug Flag to indicate if the app is in debug mode.
     * @param testDeviceIds A list of test device IDs used to show test ads.
     * @param bannerId AdMob banner ad unit ID.
     * @param appOpenAd AdMob App Open ad unit ID.
     * @param interstitialAd AdMob interstitial ad unit ID.
     * @param adaptiveBannerAd AdMob adaptive banner ad unit ID.
     * @param rewardInterstitialAd AdMob rewarded interstitial ad unit ID.
     * @param rewardAd AdMob rewarded ad unit ID.
     * @param nativeAd AdMob native ad unit ID.
     * @param collapsibleBannerAd AdMob collapsible banner ad unit ID.
     * @param excludedActivities List of activity class names to exclude from showing App Open ads.
     */

    fun initialize(
        application: Application,
        isDebug: Boolean,
        testDeviceIds: List<String> = emptyList(),
        bannerId: String = "",
        appOpenAd: String = "",
        interstitialAd: String = "",
        adaptiveBannerAd: String = "",
        rewardInterstitialAd: String = "",
        rewardAd: String = "",
        nativeAd: String = "",
        collapsibleBannerAd: String = "",
        excludedActivities: List<String> = emptyList()
    ) {
        // Set test device IDs for AdMob
        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder()
                .setTestDeviceIds(testDeviceIds)
                .build()
        )
        // Set all ad unit IDs to AdsConfig (shared config holder)
        AdsConfig.BANNER_ID = bannerId
        AdsConfig.APP_OPEN_ID = appOpenAd
        AdsConfig.IS_DEBUG = isDebug
        AdsConfig.INTERSTITIAL_AD_ID = interstitialAd
        AdsConfig.ADAPTIVE_BANNER_ID = adaptiveBannerAd
        AdsConfig.REWARD_INTERSTITIAL_AD_ID = rewardInterstitialAd
        AdsConfig.REWARDED_AD_ID = rewardAd
        AdsConfig.NATIVE_AD_ID = nativeAd
        AdsConfig.COLLAPSIBLE_BANNER_ID = collapsibleBannerAd
        AdsConfig.HashedId = testDeviceIds.first()


        // Initialize Google Mobile Ads SDK
        MobileAds.initialize(application) {
            // Once initialized, load interstitial ads if not already loaded
            loadAdOnce(::isInterstitialLoaded, { isInterstitialLoaded = true }) {
                Log.d("AdDemo", "Loading Interstitial Ad...")
                InterstitialHelper.initLoadAd(application)
            }
            // Once initialized, load reward interstitial ads if not already loaded
            loadAdOnce(::isRewardInterstitialLoaded, { isRewardInterstitialLoaded = true }) {
                Log.d("AdDemo", "Loading Rewarded Interstitial Ad...")
                RewardedInterstitialHelper.initLoadAd(application)
            }
            // Once initialized, load reward ads if not already loaded
            loadAdOnce(::isRewardLoaded, { isRewardLoaded = true }) {
                Log.d("AdDemo", "Loading Rewarded Ad...")
                RewardedAdHelper.initLoadAd(application)
            }
        }

        // Setup App Open ad handler with excluded activities
        appOpenAdHelper = AppOpenAdHelper(application, excludedActivities)

    }

    /**
     * Utility function to load an ad only once based on a flag.
     *
     * @param checkFlag Lambda returning a Boolean flag.
     * @param setFlag Lambda to update the flag once loading starts.
     * @param onLoad Lambda to call when loading should occur.
     */
    private inline fun loadAdOnce(
        checkFlag: () -> Boolean,
        setFlag: () -> Unit,
        onLoad: () -> Unit
    ) {
        if (!checkFlag()) {
            setFlag()
            onLoad()
        }
    }



    /**
     * Requests user consent using Google’s Consent SDK and invokes a callback with the result.
     * Must be called before showing ads in GDPR regions.
     *
     * @param activity The current activity context required to show consent form.
     * @param onConsentResult Callback to return whether user has granted consent:
     * - `true` = Consent granted, show ads.
     * - `false` = Consent not granted, restrict ads.
     */
    fun handleConsent(activity: Activity, onConsentResult: (Boolean) -> Unit) {
        ConsentManager.requestConsent(activity) { consentGiven ->
            onConsentResult(consentGiven)
        }
    }
}
