package com.github.xingray.uiautomatorproxy.device

import android.graphics.Rect
import com.fasterxml.jackson.annotation.JsonProperty

data class Node(

    /**
     * 这是当前节点在其父节点中的位置索引。比如，index="0" 表示这是父容器中的第一个子视图。
     */
    @JsonProperty("index")
    var index: Int = 0,

    /**
     * 该字段表示此节点的文本内容，通常用于显示文本内容的视图如 TextView 或按钮。如果视图没有显示文本，文本内容会为空字符串。
     */
    @JsonProperty("text")
    var text: String? = null,

    /**
     * 这是该 UI 元素的唯一资源 ID，通常用于标识元素在代码中。通过这个 ID，开发者可以找到并操作这个视图。
     */
    @JsonProperty("resourceId")
    var resourceId: String? = null,

    /**
     * 表示该节点的类名。android.widget.FrameLayout 是 Android 中的一个布局类，表示这个节点是一个布局容器。
     */
    @JsonProperty("className")
    var className: String? = null,

    /**
     * 该字段表示该 UI 元素所属的应用程序包名。在这里，com.kuaishou.nebula 表示该元素属于快手应用。
     */
    @JsonProperty("packageName")
    var packageName: String? = null,

    /**
     * 内容描述是用于辅助功能（如屏幕阅读器）的文本描述。当 content-desc 为空时，意味着这个视图没有提供可用于辅助功能的描述信息。
     */
    @JsonProperty("contentDesc")
    var contentDesc: String? = null,

    /**
     * 表示该元素是否可被选中（复选框或开关类型的控件）。false 表示不可选中。
     */
    @JsonProperty("checkable")
    var checkable: Boolean? = null,

    /**
     * 表示该元素是否处于选中状态。false 意味着该元素当前未被选中。
     */
    @JsonProperty("checked")
    var checked: Boolean? = null,

    /**
     * 该元素是否可以被点击。true 表示这个视图是可点击的（比如按钮）。
     */
    @JsonProperty("clickable")
    var clickable: Boolean? = null,

    /**
     * 表示该元素是否可用。true 表示这个视图当前是启用状态，用户可以与其进行交互。
     */
    @JsonProperty("enabled")
    var enabled: Boolean? = null,

    /**
     * 该元素是否可以获得焦点。true 表示这个视图可以获得焦点，用户可以通过导航键或触摸进入此视图。
     */
    @JsonProperty("focusable")
    var focusable: Boolean? = null,

    /**
     * 表示该元素当前是否处于获得焦点的状态。false 表示它没有当前的输入焦点。
     */
    @JsonProperty("focused")
    var focused: Boolean? = null,

    /**
     * 该元素是否可以滚动。false 表示它不能滚动。通常用于列表或滚动视图。
     */
    @JsonProperty("scrollable")
    var scrollable: Boolean? = null,

    /**
     * 表示该元素是否可以被长按。false 表示不能被长按。
     */
    @JsonProperty("longClickable")
    var longClickable: Boolean? = null,

    /**
     * 表示该元素是否是一个密码输入框。false 意味着这个元素不是用于输入密码的。
     */
    @JsonProperty("password")
    var password: Boolean? = null,

    /**
     * 表示该元素是否处于选中状态。false 表示它没有被选中（通常在某些列表或选项中可能用到）。
     */
    @JsonProperty("selected")
    var selected: Boolean? = null,

    /**
     * 该字段表示该元素的边界（屏幕坐标）。[31,151] 是元素左上角的坐标，[171,291] 是元素右下角的坐标。可以通过这些坐标知道该元素在屏幕上的大小和位置。
     */
    @JsonProperty("bounds")
    var bounds: Bounds? = null,

    @JsonProperty("node")
    var node: MutableList<Node>? = null,
)