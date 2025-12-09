package com.acma.acmabroadtools

import android.app.Application
import com.acma.broad.helper.AdSdkInitializer

class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val BANNER_ID = "ca-app-pub-3940256099942544/6300978111"
        val APP_OPEN_ID = "ca-app-pub-3940256099942544/9257395921"
        val INTERSTITIAL_AD_ID = "ca-app-pub-3940256099942544/1033173712"
        val ADAPTIVE_BANNER_ID = "ca-app-pub-3940256099942544/9214589741"
        val REWARD_INTERSTITIAL_AD_ID = "ca-app-pub-3940256099942544/5354046379"
        val REWARDED_AD_ID = "ca-app-pub-3940256099942544/5224354917"
        val NATIVE_AD_ID = "ca-app-pub-3940256099942544/2247696110"
        val COLLAPSIBLE_BANNER_ID = "ca-app-pub-3940256099942544/9214589741"

        val testDeviceIdsList = listOf(
            "3C3E0370B130574C31F99DCF2C1A389B", /* Samsung Galaxy S9 Plus */
            "E285DCCB0B75E97D178D508B88BB3EC6" /* Pixel 2 */
        )

        AdSdkInitializer.initialize(
            application = this,
            isDebug = true,
            testDeviceIds = testDeviceIdsList,
            bannerId = BANNER_ID,
            appOpenAd = APP_OPEN_ID,
            interstitialAd = INTERSTITIAL_AD_ID,
            adaptiveBannerAd = ADAPTIVE_BANNER_ID,
            rewardInterstitialAd = REWARD_INTERSTITIAL_AD_ID,
            rewardAd = REWARDED_AD_ID,
            nativeAd = NATIVE_AD_ID,
            collapsibleBannerAd = COLLAPSIBLE_BANNER_ID,
            excludedActivities = listOf(
//                MainActivity::class.java.canonicalName
            ),
        )


    }
}