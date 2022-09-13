package ru.kheynov.queue.api.v1

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.v1Routes() {
    route("/queue") {
        getQueueByIdRoute()
    }
}

private fun Route.getQueueByIdRoute(
) {
    get {
        val id = call.request.queryParameters["id"]?.toLong()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, "Wrong or missing ID")
            return@get
        }
    }
}