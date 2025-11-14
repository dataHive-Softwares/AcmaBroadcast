package com.acma.broad.helper.extension

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper


/**
 * Safely unwrap Activity from Context or ContextWrapper.
 */

internal fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

