package com.github.xingray.uiautomatorproxy.device

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

data class Hierarchy(
    @JsonProperty("rotation")
    @JacksonXmlProperty(localName = "rotation")
    var rotation: Int = 0,

    @JsonProperty("node")
    @JacksonXmlElementWrapper(localName = "node", useWrapping = false)
    @JacksonXmlProperty(localName = "node")
    var node: Node? = null,

    @JsonProperty("packageName")
    @JacksonXmlProperty(localName = "packageName")
    var packageName: CharSequence? = null
)