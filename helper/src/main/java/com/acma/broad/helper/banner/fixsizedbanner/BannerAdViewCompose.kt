package com.acma.broad.helper.banner.fixsizedbanner

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.acma.broad.helper.core.AdsConfig
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView


@Composable
fun BannerAdViewCompose(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    // Preview mode — no ad loading
    if (LocalInspectionMode.current) return

    val adUnitId = AdsConfig.BANNER_ID

    // Find the Activity from the context, needed for lifecycle safety if you want
    val adView = remember {
        AdView(context).apply {
            setAdSize(AdSize.BANNER)
            this.adUnitId = adUnitId
            // Load ad here, after setting adUnitId and adSize
            loadAd(AdRequest.Builder().build())
        }
    }

    AndroidView(
    factory = { adView },
    modifier = modifier
    .fillMaxWidth()
    .height(50.dp)
    )

    DisposableEffect(adView) {
        onDispose {
            adView.destroy()
        }
    }
}