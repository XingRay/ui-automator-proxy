package com.github.xingray.uiautomatorproxy.server.router

import android.app.UiAutomation
import android.graphics.Rect
import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
import androidx.test.platform.app.InstrumentationRegistry
import com.github.xingray.uiautomatorproxy.androidtest.AndroidTestHolder
import com.github.xingray.uiautomatorproxy.device.Bounds
import com.github.xingray.uiautomatorproxy.device.Hierarchy
import com.github.xingray.uiautomatorproxy.device.Node
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

private fun queryUiHierarchy(androidTestHolder: AndroidTestHolder): Hierarchy {
    Log.d(TAG, "queryUiHierarchyJson: ")
    val uiAutomation: UiAutomation = androidTestHolder.uiAutomation
    val hierarchy = Hierarchy()
    hierarchy.rotation = androidTestHolder.uiDevice.displayRotation
    val nodeInfo = uiAutomation.rootInActiveWindow
    hierarchy.node = convertNodeInfoToNode(nodeInfo, 0)
    return hierarchy
}


/**
 *
 * private static void dumpNodeRec(AccessibilityNodeInfo node, XmlSerializer serializer,int index,
 *             int width, int height) throws IOException {
 *         serializer.startTag("", "node");
 *         if (!nafExcludedClass(node) && !nafCheck(node))
 *             serializer.attribute("", "NAF", Boolean.toString(true));
 *         serializer.attribute("", "index", Integer.toString(index));
 *         serializer.attribute("", "text", safeCharSeqToString(node.getText()));
 *         serializer.attribute("", "resource-id", safeCharSeqToString(node.getViewIdResourceName()));
 *         serializer.attribute("", "class", safeCharSeqToString(node.getClassName()));
 *         serializer.attribute("", "package", safeCharSeqToString(node.getPackageName()));
 *         serializer.attribute("", "content-desc", safeCharSeqToString(node.getContentDescription()));
 *         serializer.attribute("", "checkable", Boolean.toString(node.isCheckable()));
 *         serializer.attribute("", "checked", Boolean.toString(node.isChecked()));
 *         serializer.attribute("", "clickable", Boolean.toString(node.isClickable()));
 *         serializer.attribute("", "enabled", Boolean.toString(node.isEnabled()));
 *         serializer.attribute("", "focusable", Boolean.toString(node.isFocusable()));
 *         serializer.attribute("", "focused", Boolean.toString(node.isFocused()));
 *         serializer.attribute("", "scrollable", Boolean.toString(node.isScrollable()));
 *         serializer.attribute("", "long-clickable", Boolean.toString(node.isLongClickable()));
 *         serializer.attribute("", "password", Boolean.toString(node.isPassword()));
 *         serializer.attribute("", "selected", Boolean.toString(node.isSelected()));
 *         serializer.attribute("", "visible-to-user", Boolean.toString(node.isVisibleToUser()));
 *         serializer.attribute("", "bounds", AccessibilityNodeInfoHelper.getVisibleBoundsInScreen(
 *                 node, width, height, false).toShortString());
 *         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
 *             serializer.attribute("", "drawing-order",
 *                     Integer.toString(Api24Impl.getDrawingOrder(node)));
 *         }
 *         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
 *             serializer.attribute("", "hint", safeCharSeqToString(Api26Impl.getHintText(node)));
 *         }
 *         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
 *             serializer.attribute("", "display-id",
 *                     Integer.toString(Api30Impl.getDisplayId(node)));
 *         }
 *         int count = node.getChildCount();
 *         for (int i = 0; i < count; i++) {
 *             AccessibilityNodeInfo child = node.getChild(i);
 *             if (child != null) {
 *                 if (child.isVisibleToUser()) {
 *                     dumpNodeRec(child, serializer, i, width, height);
 *                     child.recycle();
 *                 } else {
 *                     Log.i(TAG, String.format("Skipping invisible child: %s", child));
 *                 }
 *             } else {
 *                 Log.i(TAG, String.format("Null child %d/%d, parent: %s", i, count, node));
 *             }
 *         }
 *         serializer.endTag("", "node");
 *     }
 *
 */
private fun convertNodeInfoToNode(nodeInfo: AccessibilityNodeInfo?, index: Int): Node? {


    if (nodeInfo == null) {
        Log.d(TAG, "convertNodeInfoToMap: nodeInfo is null")
        return null
    }

    val node = Node()

    node.index = index
    node.text = nodeInfo.text?.toString()
    node.resourceId = nodeInfo.viewIdResourceName
    node.className = nodeInfo.className?.toString()
    node.packageName = nodeInfo.packageName?.toString()
    node.contentDesc = nodeInfo.contentDescription?.toString()
    node.checkable = nodeInfo.isCheckable
    node.checked = nodeInfo.isChecked
    node.clickable = nodeInfo.isClickable
    node.enabled = nodeInfo.isEnabled
    node.focusable = nodeInfo.isFocusable
    node.focused = nodeInfo.isFocused
    node.scrollable = nodeInfo.isScrollable
    node.longClickable = nodeInfo.isLongClickable
    node.password = nodeInfo.isPassword
    node.selected = nodeInfo.isSelected

    val rect = Rect()
    nodeInfo.getBoundsInScreen(rect)
    node.bounds = Bounds(rect.left, rect.top, rect.right, rect.bottom)

    // 遍历子节点
    val childNodes = mutableListOf<Node>()
    for (i in 0 until nodeInfo.childCount) {
        val child = nodeInfo.getChild(i) ?: continue
        val childNode = convertNodeInfoToNode(child, i) ?: continue
        childNodes.add(childNode)
    }
    node.node = childNodes

    return node
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
