package com.github.xingray.uiautomatorproxy.kotlin

fun Boolean.trueOrNull(): Boolean? {
    return if (this) {
        true
    } else {
        null
    }
}