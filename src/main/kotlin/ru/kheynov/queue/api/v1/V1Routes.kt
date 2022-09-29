package ru.kheynov.queue.api.v1

import io.ktor.server.routing.*
import ru.kheynov.queue.api.v1.routing.roomRoutes
import ru.kheynov.queue.api.v1.routing.userRoutes
import ru.kheynov.queue.di.ServiceLocator
import ru.kheynov.queue.domain.use_cases.UseCases

fun Route.v1Routes(
    useCases: UseCases = ServiceLocator.useCases,
) {
    route("/v1") {
        userRoutes(useCases)
        roomRoutes(useCases)
    }
}