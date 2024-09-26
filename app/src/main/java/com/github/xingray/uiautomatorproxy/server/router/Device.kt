package com.github.xingray.uiautomatorproxy.server.router

import android.app.UiAutomation
import android.graphics.Rect
import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
import androidx.test.platform.app.InstrumentationRegistry
import com.github.xingray.uiautomatorproxy.androidtest.AndroidTestHolder
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import java.io.ByteArrayInputStream


private val TAG = "device"

fun Application.device(androidTestHolder: AndroidTestHolder) {
    routing {
        route("/device") {
            route("/hierarchy") {
                get {
                    Log.d(TAG, "device: /hierarchy")
                    call.respond(queryUiHierarchy(androidTestHolder))
                }
            }
        }
    }
}

//private fun queryUiHierarchy(outputStream: OutputStream) {
//    // 启动监听命令的线程
//    val instrumentation = InstrumentationRegistry.getInstrumentation()
//    val uiAutomation = instrumentation.uiAutomation
//
//    val nodeInfo = uiAutomation.rootInActiveWindow
//    val nodeInfoValue: Map<String, Any?> = convertNodeInfoToMap(nodeInfo)
//    val objectMapper = ObjectMapper()
//    objectMapper.writeValue(outputStream, nodeInfoValue)
//}

private fun queryUiHierarchy(androidTestHolder: AndroidTestHolder): Map<String, Any?> {
    Log.d(TAG, "queryUiHierarchyJson: ")
    val uiAutomation: UiAutomation = androidTestHolder.uiAutomation
    val nodeInfo = uiAutomation.rootInActiveWindow
    return convertNodeInfoToMap(nodeInfo)
}

private fun convertNodeInfoToMap(nodeInfo: AccessibilityNodeInfo?): Map<String, Any?> {
    val nodeInfoMap: MutableMap<String, Any?> = HashMap()

    if (nodeInfo == null) {
        Log.d(TAG, "convertNodeInfoToMap: nodeInfo is null")
        return nodeInfoMap
    }

    nodeInfoMap["className"] = nodeInfo.className
    nodeInfoMap["text"] = nodeInfo.text
    nodeInfoMap["contentDescription"] = nodeInfo.contentDescription
    nodeInfoMap["viewIdResourceName"] = nodeInfo.viewIdResourceName
    nodeInfoMap["clickable"] = nodeInfo.isClickable
    nodeInfoMap["enabled"] = nodeInfo.isEnabled
    nodeInfoMap["focused"] = nodeInfo.isFocused
    nodeInfoMap["packageName"] = nodeInfo.packageName
    val rect = Rect()
    nodeInfo.getBoundsInScreen(rect)
    nodeInfoMap["bounds"] = getBoundsAsMap(rect)

    // 遍历子节点
    val childNodes: MutableList<Map<String, Any?>> = ArrayList()
    for (i in 0 until nodeInfo.childCount) {
        val child = nodeInfo.getChild(i)
        if (child != null) {
            childNodes.add(convertNodeInfoToMap(child))
        }
    }
    nodeInfoMap["children"] = childNodes

    return nodeInfoMap
}

private fun getBoundsAsMap(rect: Rect): Map<String, Int> {
    val boundsMap: MutableMap<String, Int> = HashMap()
    boundsMap["left"] = rect.left
    boundsMap["top"] = rect.top
    boundsMap["right"] = rect.right
    boundsMap["bottom"] = rect.bottom
    return boundsMap
}

private fun screenshot() {
    val params = HashMap<String, String>()
    var scale = 1.0f
    if (params.containsKey("scale")) {
        try {
            scale = params.get("scale")?.toFloat() ?: 1.0f
        } catch (e: NumberFormatException) {
        }
    }
    var quality = 100
    if (params.containsKey("quality")) {
        try {
            quality = params.get("quality")?.toInt() ?: 100
        } catch (e: NumberFormatException) {
        }
    }


    //            File f = new File(InstrumentationRegistry.getTargetContext().getFilesDir(), "screenshot.png");
//            UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
//            uiDevice.takeScreenshot(f, scale, quality);

//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
    val instrumentation = InstrumentationRegistry.getInstrumentation()
    val uiAutomation = instrumentation.uiAutomation

    val start1 = System.currentTimeMillis()
    val screenshot = uiAutomation.takeScreenshot()
    val time1 = System.currentTimeMillis() - start1
    Log.d(TAG, "serve: time1: $time1")


    //            long start2 = System.currentTimeMillis();
//            screenshot.compress(Bitmap.CompressFormat.JPEG, quality, bos);
//            long time2 = System.currentTimeMillis() - start2;
    val start3 = System.currentTimeMillis()
    val width = screenshot.width
    val height = screenshot.height
    val pixels = IntArray(width * height)
    screenshot.getPixels(pixels, 0, width, 0, 0, width, height)
    val time3 = System.currentTimeMillis() - start3
    Log.d(TAG, "serve: time3: $time3")

    val start4 = System.currentTimeMillis()

    // 创建字节数组用于存储像素数据
    val imageData = ByteArray(width * height * 4) // 每个像素 4 个字节 (ARGB)
    for (i in pixels.indices) {
        imageData[i * 4] = ((pixels[i] shr 16) and 0xFF).toByte() // R
        imageData[i * 4 + 1] = ((pixels[i] shr 8) and 0xFF).toByte() // G
        imageData[i * 4 + 2] = (pixels[i] and 0xFF).toByte() // B
        imageData[i * 4 + 3] = ((pixels[i] shr 24) and 0xFF).toByte() // A
    }
    val time4 = System.currentTimeMillis() - start4
    Log.d(TAG, "serve: time4: $time4")


    //
//            try {
//                bos.flush();
//            } catch (IOException e) {
//                Log.e(e.getMessage());
//                return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, "Internal Server Error!!!");
//            }
//            return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, "Internal Server Error!!!, time1:" + time1);
    val byteArrayInputStream = ByteArrayInputStream(imageData)
//        return newChunkedResponse(Response.Status.OK, "application/octet-stream", byteArrayInputStream)
}
