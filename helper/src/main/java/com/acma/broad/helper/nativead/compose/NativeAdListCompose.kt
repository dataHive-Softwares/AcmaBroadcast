package com.acma.broad.helper.nativead.compose

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.acma.broad.helper.core.AdsConfig


@Composable
fun <T> NativeAdListCompose(
    items: List<T>,
    frequency: Int = 5,
    adSize: NativeAdSize = NativeAdSize.MEDIUM,
    itemContent: @Composable (T) -> Unit
) {

    val shouldShowAds =  AdsConfig.shouldEnableNativeAds().value
    if (shouldShowAds?.not() ?: true) return

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        itemsIndexed(items) { index, item ->
            // Show the regular item
            itemContent(item)

            // Show the ad after every [frequency] items
            if ((index + 1) % frequency == 0) {
                Spacer(modifier = Modifier.height(8.dp))
                NativeAdViewCompose(adSize = adSize)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}