package com.acma.broad.helper.core

import android.content.Context

interface AdLoader {
    val state: AdLoadState
    fun load(context: Context)
}