package com.github.xingray.uiautomatorproxy.server.router

import com.github.xingray.uiautomatorproxy.BuildConfig
import io.ktor.http.ContentType
import io.ktor.server.application.Application
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun Application.info() {
    routing {
        get("/hello") {
            call.respondText("Hello, ui-automator-proxy server", ContentType.Text.Plain)
        }

        get("/version") {
            call.respondText(BuildConfig.VERSION_NAME, ContentType.Text.Plain)
        }
    }
}