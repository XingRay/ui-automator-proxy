package com.github.xingray.uiautomatorproxy.server.router

import com.github.xingray.uiautomatorproxy.androidtest.AndroidTestHolder
import com.github.xingray.uiautomatorproxy.device.Device
import io.ktor.http.ContentType
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respondOutputStream
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing


private val TAG = "device"

fun Application.device(androidTestHolder: AndroidTestHolder) {
    routing {
        route("/device") {
            route("/hierarchy") {
                route("/xml") {
                    get {
                        call.respondOutputStream(ContentType.Application.Xml) {
                            Device().writeUiHierarchyAsXml(androidTestHolder, this)
                        }
                    }
                }

                route("/json") {
                    route("full") {
                        get {
                            call.respondOutputStream(ContentType.Application.Json) {
                                Device().writeFullUiHierarchyAsJson(androidTestHolder, this)
                            }
                        }
                    }

                    route("/simple") {
                        get {
                            call.respondOutputStream(ContentType.Application.Json) {
                                Device().writeSimpleUiHierarchyAsJson(androidTestHolder, this)
                            }
                        }
                    }
                }
            }

            route("/screenshot") {
                get {
                    val format = call.request.queryParameters["format"]?.lowercase() ?: "jpg"
                    val quality = call.request.queryParameters["quality"]?.toIntOrNull() ?: 100
                    val contentType = when (format) {
                        "png" -> ContentType.Image.PNG
                        "jpg", "jpeg" -> ContentType.Image.JPEG
                        else -> ContentType.Image.Any
                    }

                    call.respondOutputStream(contentType) {
                        Device().writeScreenShot(androidTestHolder, this, format, quality)
                    }
                }
            }
        }
    }
}
