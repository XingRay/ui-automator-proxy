package com.github.xingray.uiautomatorproxy.server.router

import io.ktor.http.ContentType
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun Application.hello() {
    routing {
        get("/hello") {
            call.respondText("Hello, ui-automator-proxy server", ContentType.Text.Plain)
        }
    }
}