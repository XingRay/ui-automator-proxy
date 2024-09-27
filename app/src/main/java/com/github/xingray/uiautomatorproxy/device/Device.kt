package com.github.xingray.uiautomatorproxy.device

import android.app.UiAutomation
import android.graphics.Rect
import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
import androidx.test.platform.app.InstrumentationRegistry
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.uiautomator.AccessibilityNodeInfoDumper
import com.github.xingray.uiautomatorproxy.androidtest.AndroidTestHolder
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.OutputStream

class Device() {

    companion object {
        @JvmStatic
        private val TAG = Device::class.java.simpleName
    }

    fun writeUiHierarchyAsXml(androidTestHolder: AndroidTestHolder, outputStream: OutputStream) {
        androidTestHolder.uiDevice.dumpWindowHierarchy(outputStream)
    }

    fun writeUiHierarchyAsXmlString(androidTestHolder: AndroidTestHolder, compressed: Boolean = true, maxDepth: Int = 50): String? {
        // 启动监听命令的线程
        androidTestHolder.uiDevice.setCompressedLayoutHierarchy(compressed)
        val os = ByteArrayOutputStream()
        try {
            AccessibilityNodeInfoDumper.dumpWindowHierarchy(androidTestHolder.uiDevice, os, maxDepth)
            return os.toString("UTF-8")
        } catch (e: IOException) {
            Log.d(TAG, "writeUiHierarchyAsXml: dumpWindowHierarchy got IOException: $e")
        } finally {
            try {
                os.close()
            } catch (e: IOException) {
                // ignore
            }
        }

        return null
    }


    fun writeFullUiHierarchyAsJson(androidTestHolder: AndroidTestHolder, outputStream: OutputStream) {
        Log.d(TAG, "writeFullUiHierarchyAsJson: ")
        val uiAutomation: UiAutomation = androidTestHolder.uiAutomation
        val startTimeMills = System.currentTimeMillis()
        val hierarchy = Hierarchy()
        hierarchy.rotation = androidTestHolder.uiDevice.displayRotation
        val nodeInfo: AccessibilityNodeInfo? = uiAutomation.rootInActiveWindow
        if (nodeInfo != null) {
            hierarchy.node = convertNodeInfoToNodeFull(nodeInfo, 0)
        }
        val time = System.currentTimeMillis() - startTimeMills
        Log.d(TAG, "queryUiHierarchy: time:${time} ms")
        val objectMapper = ObjectMapper()
        objectMapper.writeValue(outputStream, hierarchy)
    }

    fun writeSimpleUiHierarchyAsJson(androidTestHolder: AndroidTestHolder, outputStream: OutputStream) {
        Log.d(TAG, "writeSimpleUiHierarchyAsJson: ")
        val uiAutomation: UiAutomation = androidTestHolder.uiAutomation
        val startTimeMills = System.currentTimeMillis()
        val hierarchy = Hierarchy()
        hierarchy.rotation = androidTestHolder.uiDevice.displayRotation
        val nodeInfo: AccessibilityNodeInfo? = uiAutomation.rootInActiveWindow
        if (nodeInfo != null) {
            hierarchy.node = convertNodeInfoToNodeSimple(nodeInfo, 0)
        }
        val time = System.currentTimeMillis() - startTimeMills
        Log.d(TAG, "queryUiHierarchy: time:${time} ms")
        val objectMapper = ObjectMapper()
        objectMapper.writeValue(outputStream, hierarchy)
    }


    fun queryUiHierarchy(androidTestHolder: AndroidTestHolder): Hierarchy {
        Log.d(TAG, "queryUiHierarchyJson: ")
        val uiAutomation: UiAutomation = androidTestHolder.uiAutomation
        val startTimeMills = System.currentTimeMillis()
        val hierarchy = Hierarchy()
//    androidTestHolder.uiDevice.dumpWindowHierarchy()
        hierarchy.rotation = androidTestHolder.uiDevice.displayRotation
        val nodeInfo: AccessibilityNodeInfo? = uiAutomation.rootInActiveWindow
        if (nodeInfo != null) {
            hierarchy.node = convertNodeInfoToNodeFull(nodeInfo, 0)
        }
        val time = System.currentTimeMillis() - startTimeMills
        Log.d(TAG, "queryUiHierarchy: time:${time} ms")
        return hierarchy
    }

    fun convertNodeInfoToNodeFull(nodeInfo: AccessibilityNodeInfo, index: Int): Node? {
        val rect = Rect()
        nodeInfo.getBoundsInScreen(rect)
        val bounds = Bounds(rect.left, rect.top, rect.right, rect.bottom)

        val node = Node()

//    node.index = index
        node.text = nodeInfo.text
        node.resourceId = nodeInfo.viewIdResourceName
        node.className = nodeInfo.className
//    node.packageName = nodeInfo.packageName?.toString()
        node.contentDesc = nodeInfo.contentDescription
//    node.checkable = nodeInfo.isCheckable.trueOrNull()
//    node.checked = nodeInfo.isChecked.trueOrNull()
//    node.clickable = nodeInfo.isClickable.trueOrNull()
//    node.enabled = nodeInfo.isEnabled.trueOrNull()
//    node.focusable = nodeInfo.isFocusable.trueOrNull()
//    node.focused = nodeInfo.isFocused.trueOrNull()
//    node.scrollable = nodeInfo.isScrollable.trueOrNull()
//    node.longClickable = nodeInfo.isLongClickable.trueOrNull()
//    node.password = nodeInfo.isPassword.trueOrNull()
//    node.selected = nodeInfo.isSelected.trueOrNull()
        node.bounds = bounds


        // 遍历子节点
        val childNodes = mutableListOf<Node>()
        for (i in 0 until nodeInfo.childCount) {
            val child = nodeInfo.getChild(i) ?: continue
            val childNode = convertNodeInfoToNodeFull(child, i) ?: continue
            childNodes.add(childNode)
        }
        if (childNodes.isNotEmpty()) {
            node.node = childNodes
        }

        return node
    }

    fun convertNodeInfoToNodeSimple(nodeInfo: AccessibilityNodeInfo, index: Int): Node? {
        val rect = Rect()
        nodeInfo.getBoundsInScreen(rect)
        if (rect.isEmpty) {
            return null
        }
        val bounds = Bounds(rect.left, rect.top, rect.right, rect.bottom)

        val node = Node()

//    node.index = index
        node.text = nodeInfo.text
        node.resourceId = nodeInfo.viewIdResourceName
        node.className = nodeInfo.className
//    node.packageName = nodeInfo.packageName?.toString()
        node.contentDesc = nodeInfo.contentDescription
//    node.checkable = nodeInfo.isCheckable.trueOrNull()
//    node.checked = nodeInfo.isChecked.trueOrNull()
//    node.clickable = nodeInfo.isClickable.trueOrNull()
//    node.enabled = nodeInfo.isEnabled.trueOrNull()
//    node.focusable = nodeInfo.isFocusable.trueOrNull()
//    node.focused = nodeInfo.isFocused.trueOrNull()
//    node.scrollable = nodeInfo.isScrollable.trueOrNull()
//    node.longClickable = nodeInfo.isLongClickable.trueOrNull()
//    node.password = nodeInfo.isPassword.trueOrNull()
//    node.selected = nodeInfo.isSelected.trueOrNull()
        node.bounds = bounds


        // 遍历子节点
        val childNodes = mutableListOf<Node>()
        for (i in 0 until nodeInfo.childCount) {
            val child = nodeInfo.getChild(i) ?: continue
            val childNode = convertNodeInfoToNodeFull(child, i) ?: continue
            childNodes.add(childNode)
        }
        if (childNodes.isNotEmpty()) {
            node.node = childNodes
        }

        return node
    }


    fun screenshot() {
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
}