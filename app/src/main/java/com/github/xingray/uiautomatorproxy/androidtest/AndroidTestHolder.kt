package com.github.xingray.uiautomatorproxy.androidtest

import android.app.Instrumentation
import android.app.UiAutomation
import androidx.test.uiautomator.UiDevice

data class AndroidTestHolder(
    val instrumentation: Instrumentation,
    val uiAutomation: UiAutomation,
    val uiDevice: UiDevice,

)