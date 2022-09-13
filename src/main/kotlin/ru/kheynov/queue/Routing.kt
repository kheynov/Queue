package ru.kheynov.queue

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.kheynov.queue.api.v1.v1Routes

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        v1Routes()
    }
}