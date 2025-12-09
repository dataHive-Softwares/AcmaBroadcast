package com.acma.broad.helper.core


sealed class AdLoadState {
    object Idle : AdLoadState()
    object Loading : AdLoadState()
    object Loaded : AdLoadState()
    data class Error(val message: String? = null) : AdLoadState()
}