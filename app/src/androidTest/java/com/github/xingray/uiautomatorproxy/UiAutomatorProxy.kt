package com.github.xingray.uiautomatorproxy

import android.app.Instrumentation
import android.app.UiAutomation
import android.content.Context.WINDOW_SERVICE
import android.util.Log
import android.view.WindowManager
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.github.xingray.uiautomatorproxy.androidtest.AndroidTestHolder
import com.github.xingray.uiautomatorproxy.androidtest.getUiAutomationOrNull
import com.github.xingray.uiautomatorproxy.androidtest.getUiDeviceOrNull
import com.github.xingray.uiautomatorproxy.server.UiAutomatorProxyServer
import org.junit.Before
import org.junit.Test
import java.io.File

//@RunWith(AndroidJUnit4::class)
class UiAutomatorProxy {

    companion object {
        @JvmStatic
        private val TAG = UiAutomatorProxy::class.java.simpleName
    }

    @Before
    fun setUp() {


    }

    @Test
    fun startProxyServer() {
        val instrumentation: Instrumentation? = InstrumentationRegistry.getInstrumentation()
        if (instrumentation == null) {
            Log.d(TAG, "startProxyServer: instrumentation == null")
            return
        } else {
            Log.d(TAG, "startProxyServer: instrumentation:${instrumentation}")
        }

//        @rem -w 参数不能少, 没有 -w 参数, 无法获取 UiAutomation 和  UiDevice 对象
//        call adb shell am instrument -w -r -e debug false -e class "%PACKAGE_NAME%.%TEST_CLASS_NAME%" "%PACKAGE_NAME%.test/androidx.test.runner.AndroidJUnitRunner"

        val uiAutomation: UiAutomation? = instrumentation.getUiAutomationOrNull()
        if (uiAutomation == null) {
            Log.d(TAG, "startProxyServer: uiAutomation == null")
        } else {
            Log.d(TAG, "startProxyServer: uiAutomation:${uiAutomation}")
        }

        val uiDevice: UiDevice? = instrumentation.getUiDeviceOrNull()
        if (uiDevice == null) {
            Log.d(TAG, "startProxyServer: uiDevice == null")
        } else {
            Log.d(TAG, "startProxyServer: uiDevice:${uiDevice}")
        }

        val targetContext = instrumentation.targetContext
        if (targetContext == null) {
            Log.d(TAG, "startProxyServer: targetContext == null")
        } else {
            Log.d(TAG, "startProxyServer: targetContext:${targetContext}")

            val cacheDir: File? = targetContext.cacheDir
            if (cacheDir == null) {
                Log.d(TAG, "startProxyServer: cacheDir == null")
            } else {
                Log.d(TAG, "startProxyServer: cacheDir:${cacheDir.absolutePath}")
            }

            val service = targetContext.getSystemService(WINDOW_SERVICE)
            if (service != null && service is WindowManager) {
                val windowManager = service
                Log.d(TAG, "startProxyServer: windowManager:${windowManager}")
            } else {
                Log.d(TAG, "startProxyServer: windowManager == null, service:${service}")
            }
        }

        if (uiAutomation != null && uiDevice != null) {
            val androidTestHolder = AndroidTestHolder(instrumentation, uiAutomation, uiDevice)
            UiAutomatorProxyServer(androidTestHolder).start()
        }
    }
}
