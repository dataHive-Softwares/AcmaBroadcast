package com.acma.broad.helper.data

data class AdsConfig(
    val bannerId: String = "",
    val interstitialId: String = "",
    val adaptiveBannerId: String = "",
    val rewardInterstitialId: String = "",
    val rewardedAdId: String = "",
    val nativeId: String = "",
    val collapsibleBannerId: String = "",
    val appOpenId: String = "",
    val isDebug: Boolean = false,
    val testDeviceIds: List<String> = emptyList()
)
