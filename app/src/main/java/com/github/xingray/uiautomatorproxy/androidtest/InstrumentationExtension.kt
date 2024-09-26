package com.github.xingray.uiautomatorproxy.androidtest

import android.app.Instrumentation
import android.app.UiAutomation
import android.util.Log
import androidx.test.uiautomator.UiDevice

private val TAG = "InstrumentationExtension"

fun Instrumentation.getUiAutomationOrNull(): UiAutomation? {
    return try {
        uiAutomation
    } catch (e: Exception) {
        Log.e(TAG, "getUiAutomationOrNull: e:${e.message}")
        null
    }
}


fun Instrumentation.getUiDeviceOrNull(): UiDevice? {
    return try {
        UiDevice.getInstance(this)
    } catch (e: Exception) {
        Log.e(TAG, "getUiDeviceOrNull: e:${e.message}")
        null
    }
}
