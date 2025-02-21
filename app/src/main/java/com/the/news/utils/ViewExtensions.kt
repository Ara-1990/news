package com.the.news.utils

import android.view.View


fun View.visibleOrGone(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

private var isClickEnabled: Boolean = true
private val enable: () -> Unit = { isClickEnabled = true }

fun View.setOnDebouncedClickListener(delayTime: Long = DELAY_DEBOUNCE_CLICK, action: () -> Unit) {
    this.setOnClickListener { v ->
        if (isClickEnabled) {
            isClickEnabled = false
            v.postDelayed(enable, delayTime)
            action.invoke()
        }
    }
}