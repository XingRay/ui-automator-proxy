package com.github.xingray.uiautomatorproxy.device

import android.app.UiAutomation
import android.graphics.Bitmap
import android.graphics.Rect
import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.uiautomator.AccessibilityNodeInfoDumper
import com.github.xingray.uiautomatorproxy.androidtest.AndroidTestHolder
import com.github.xingray.uiautomatorproxy.kotlin.trueOrNull
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
        if (rect.isEmpty) {
            return null
        }
        val bounds = Bounds(rect.left, rect.top, rect.right, rect.bottom)
        val node = Node()

        node.index = index
        node.text = nodeInfo.text
        node.resourceId = nodeInfo.viewIdResourceName
        node.className = nodeInfo.className
//    node.packageName = nodeInfo.packageName?.toString()
        node.contentDesc = nodeInfo.contentDescription
        node.checkable = nodeInfo.isCheckable.trueOrNull()
        node.checked = nodeInfo.isChecked.trueOrNull()
        node.clickable = nodeInfo.isClickable.trueOrNull()
        node.enabled = nodeInfo.isEnabled.trueOrNull()
        node.focusable = nodeInfo.isFocusable.trueOrNull()
        node.focused = nodeInfo.isFocused.trueOrNull()
        node.scrollable = nodeInfo.isScrollable.trueOrNull()
        node.longClickable = nodeInfo.isLongClickable.trueOrNull()
        node.password = nodeInfo.isPassword.trueOrNull()
        node.selected = nodeInfo.isSelected.trueOrNull()
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
//        node.checkable = nodeInfo.isCheckable.trueOrNull()
//        node.checked = nodeInfo.isChecked.trueOrNull()
//        node.clickable = nodeInfo.isClickable.trueOrNull()
//        node.enabled = nodeInfo.isEnabled.trueOrNull()
//        node.focusable = nodeInfo.isFocusable.trueOrNull()
//        node.focused = nodeInfo.isFocused.trueOrNull()
//        node.scrollable = nodeInfo.isScrollable.trueOrNull()
//        node.longClickable = nodeInfo.isLongClickable.trueOrNull()
//        node.password = nodeInfo.isPassword.trueOrNull()
        node.selected = nodeInfo.isSelected.trueOrNull()
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

    fun writeScreenShot(androidTestHolder: AndroidTestHolder, outputStream: OutputStream, format: String, quality: Int = 100) {
        val bitmap: Bitmap? = androidTestHolder.uiAutomation.takeScreenshot()
        if (bitmap == null) {
            Log.d(TAG, "writeScreenShot: can not takeScreenshot")
            outputStream.flush()
            return
        }

        val compressFormat = when (format) {
            "png" -> {
                Bitmap.CompressFormat.PNG
            }

            "jpg", "jpeg" -> {
                Bitmap.CompressFormat.JPEG
            }

            else -> {
                Log.d(TAG, "writeScreenShot: unkonown type")
                outputStream.flush()
                return
            }
        }

        bitmap.compress(compressFormat, quality, outputStream)
    }
}