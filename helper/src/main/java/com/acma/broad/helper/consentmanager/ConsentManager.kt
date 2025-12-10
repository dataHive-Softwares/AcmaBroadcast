package com.acma.broad.helper.consentmanager

import android.app.Activity
import com.acma.broad.helper.core.AdsConfig
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform

internal object ConsentManager {

    var TEST_DEVICE_HASHED_ID = AdsConfig.HashedId

    fun requestConsent(activity: Activity, onConsentResult: (Boolean) -> Unit) {
        val consentInfo = UserMessagingPlatform.getConsentInformation(activity)

        // Skip consent form for production if not required
        if (!AdsConfig.IS_DEBUG && consentInfo.isNotRequired) {
            onConsentResult(consentInfo.canRequestAds())
            return
        }

        val debugSettings = ConsentDebugSettings.Builder(activity).apply {
            if (AdsConfig.IS_DEBUG) {
                setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
                TEST_DEVICE_HASHED_ID.forEach { addTestDeviceHashedId(it) }
            }
        }.build()

        val params = ConsentRequestParameters.Builder()
            .setConsentDebugSettings(debugSettings)
            .build()

        consentInfo.requestConsentInfoUpdate(activity, params, {
            if (consentInfo.isConsentFormAvailable &&
                consentInfo.consentStatus == ConsentInformation.ConsentStatus.REQUIRED
            ) {
                loadAndShowConsentForm(activity, consentInfo, onConsentResult)
            } else {
                onConsentResult(consentInfo.canRequestAds())
            }
        }, {
            onConsentResult(false)
        })
    }

    private fun loadAndShowConsentForm(
        activity: Activity,
        consentInfo: ConsentInformation,
        onConsentResult: (Boolean) -> Unit
    ) {
        UserMessagingPlatform.loadConsentForm(activity, { form ->
            form.show(activity) {
                onConsentResult(consentInfo.canRequestAds())
            }
        }, {
            onConsentResult(false)
        })
    }

    private val ConsentInformation.isRequired: Boolean
        get() = consentStatus == ConsentInformation.ConsentStatus.REQUIRED

    private val ConsentInformation.isNotRequired: Boolean
        get() = isRequired.not()
}
