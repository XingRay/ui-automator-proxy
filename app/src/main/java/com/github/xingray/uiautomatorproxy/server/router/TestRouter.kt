package com.github.xingray.uiautomatorproxy.server.router

import io.ktor.http.ContentType
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun Application.test(){
    routing {
        get("/") {
            call.respondText("Hello, Ktor!", ContentType.Text.Plain)
        }
    }

    routing {
        get("/aaa") {
            call.respondText("Hello, Ktor! AAA", ContentType.Text.Plain)
        }
    }

    routing {
        get("/bbb") {
            call.respondText("Hello, Ktor! BBB", ContentType.Text.Plain)
        }
    }

    routing {
        get("/ccc") {
            call.respondText("Hello, Ktor! CCC", ContentType.Text.Plain)
        }
    }
}