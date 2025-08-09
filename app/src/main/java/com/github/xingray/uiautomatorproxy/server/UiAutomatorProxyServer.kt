package com.github.xingray.uiautomatorproxy.server

import android.util.Log
import com.fasterxml.jackson.core.util.DefaultIndenter
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.github.xingray.uiautomatorproxy.androidtest.AndroidTestHolder
import com.github.xingray.uiautomatorproxy.server.router.device
import com.github.xingray.uiautomatorproxy.server.router.info
import com.github.xingray.uiautomatorproxy.server.router.test
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStarted
import io.ktor.server.application.ApplicationStarting
import io.ktor.server.application.ApplicationStopPreparing
import io.ktor.server.application.ApplicationStopped
import io.ktor.server.application.ApplicationStopping
import io.ktor.server.application.install
import io.ktor.server.engine.ShutDownUrl
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS

class UiAutomatorProxyServer(val androidTestHolder: AndroidTestHolder) {

    companion object {
        @JvmStatic
        private val TAG = UiAutomatorProxyServer::class.java.simpleName
    }

    private val PORT = 51234

    fun start() {
        Log.d(TAG, "UiAutomatorProxyServer#start()")

        Netty.configuration {
            shareWorkGroup = false
            configureBootstrap = {
                //...
            }
            responseWriteTimeoutSeconds = 10
        }

        embeddedServer(Netty, port = PORT) {
            installPlugins()
            monitor()
            addRouter()
        }.start(wait = true)
    }

    private fun Application.installPlugins() {
        install(ShutDownUrl.ApplicationCallPlugin) {
            shutDownUrl = "/shutdown"
            exitCodeSupplier = { 0 }
        }

        install(CORS)
        install(CallLogging)

        install(ContentNegotiation) {
            jackson {
                configure(SerializationFeature.INDENT_OUTPUT, true)
                setDefaultPrettyPrinter(DefaultPrettyPrinter().apply {
                    indentArraysWith(DefaultPrettyPrinter.FixedSpaceIndenter.instance)
                    indentObjectsWith(DefaultIndenter("  ", "\n"))
                })
                registerModule(JavaTimeModule())  // support java.time.* types
            }
        }
    }

    private fun Application.monitor() {
        monitor.subscribe(ApplicationStarting) {
            Log.d(TAG, "monitor: ApplicationStarting")

        }

        monitor.subscribe(ApplicationStarted) {
            Log.d(TAG, "monitor: ApplicationStarted")

        }

        monitor.subscribe(ApplicationStopPreparing) {
            Log.d(TAG, "monitor: ApplicationStopPreparing")

        }

        monitor.subscribe(ApplicationStopping) {
            Log.d(TAG, "monitor: ApplicationStopping")

        }

        monitor.subscribe(ApplicationStopped) {
            Log.d(TAG, "monitor: ApplicationStopped")

        }
    }

    private fun Application.addRouter() {
        info()
        test()
        device(androidTestHolder)
    }
}