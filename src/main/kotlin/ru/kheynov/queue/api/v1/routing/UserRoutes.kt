package ru.kheynov.queue.api.v1.routing

import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.Conflict
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.kheynov.queue.api.v1.requests.DeleteUserRequest
import ru.kheynov.queue.api.v1.requests.RegisterUserRequest
import ru.kheynov.queue.di.ServiceLocator
import ru.kheynov.queue.domain.entities.User
import ru.kheynov.queue.domain.use_cases.UseCases
import ru.kheynov.queue.domain.use_cases.users.DeleteUserUseCase
import ru.kheynov.queue.domain.use_cases.users.RegisterUserUseCase
import ru.kheynov.queue.security.TokenVerifier

fun Route.userRoutes(useCases: UseCases) {
    registerUser(useCases.registerUserUseCase)
    deleteUser(useCases.deleteUserUseCase)
}

private fun Route.registerUser(
    registerUserUseCase: RegisterUserUseCase,
    tokenVerifier: TokenVerifier = ServiceLocator.tokenVerifier,
) {
    post {
        val request = call.receiveNullable<RegisterUserRequest>() ?: run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        val userId = when (val verificationResult = tokenVerifier.verifyToken(request.token)) {
            is TokenVerifier.Result.Correct -> verificationResult.userId
            TokenVerifier.Result.Incorrect -> {
                call.respond(HttpStatusCode.BadRequest, "Invalid token")
                return@post
            }
        }

        if (registerUserUseCase(User(name = request.name, vkID = userId))) {
            call.respond(OK)
            return@post
        }
        call.respond(Conflict, "Cannot register user")
        return@post
    }
}

private fun Route.deleteUser(
    deleteUserUseCase: DeleteUserUseCase,
    tokenVerifier: TokenVerifier = ServiceLocator.tokenVerifier,
) {
    delete {
        val request = call.receiveNullable<DeleteUserRequest>() ?: run {
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }
        val userId = when (val verificationResult = tokenVerifier.verifyToken(request.token)) {
            is TokenVerifier.Result.Correct -> verificationResult.userId
            TokenVerifier.Result.Incorrect -> {
                call.respond(HttpStatusCode.BadRequest, "Invalid token")
                return@delete
            }
        }
        if (deleteUserUseCase(userId)) {
            call.respond(OK)
            return@delete
        }
        call.respond(Conflict, "Cannot register user")
        return@delete
    }
}
