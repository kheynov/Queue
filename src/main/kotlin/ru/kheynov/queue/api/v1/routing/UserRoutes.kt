package ru.kheynov.queue.api.v1.routing

import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.Conflict
import io.ktor.http.HttpStatusCode.Companion.NotFound
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.kheynov.queue.api.v1.requests.user.RegisterUserRequest
import ru.kheynov.queue.api.v1.requests.user.RequestWithToken
import ru.kheynov.queue.di.ServiceLocator
import ru.kheynov.queue.domain.entities.User
import ru.kheynov.queue.domain.use_cases.UseCases
import ru.kheynov.queue.domain.use_cases.users.DeleteUserUseCase
import ru.kheynov.queue.domain.use_cases.users.GetUserRoomsUseCase
import ru.kheynov.queue.domain.use_cases.users.RegisterUserUseCase
import ru.kheynov.queue.security.TokenVerifier

fun Route.userRoutes(useCases: UseCases) {
    route("/user") {
        registerUser(useCases.registerUserUseCase)
        deleteUser(useCases.deleteUserUseCase)
        getUserRooms(useCases.getUserRoomsUseCase)
    }
}

private fun Route.registerUser(
    registerUserUseCase: RegisterUserUseCase,
    tokenVerifier: TokenVerifier = ServiceLocator.tokenVerifier,
) {
    post {
        val request = call.receiveNullable<RegisterUserRequest>() ?: run {
            call.respond(BadRequest)
            return@post
        }
        val userId = when (val verificationResult = tokenVerifier.verifyToken(request.token)) {
            is TokenVerifier.Result.Correct -> verificationResult.userId
            TokenVerifier.Result.Incorrect -> {
                call.respond(BadRequest, "Invalid token")
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
        val request = call.receiveNullable<RequestWithToken>() ?: run {
            call.respond(BadRequest)
            return@delete
        }
        val userId = when (val verificationResult = tokenVerifier.verifyToken(request.token)) {
            is TokenVerifier.Result.Correct -> verificationResult.userId
            TokenVerifier.Result.Incorrect -> {
                call.respond(BadRequest, "Invalid token")
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

private fun Route.getUserRooms(
    getUserRoomsUseCase: GetUserRoomsUseCase,
    tokenVerifier: TokenVerifier = ServiceLocator.tokenVerifier,
) {
    get("/rooms") {
        val request = call.receiveNullable<RequestWithToken>() ?: run {
            call.respond(BadRequest)
            return@get
        }
        val userId = when (val verificationResult = tokenVerifier.verifyToken(request.token)) {
            is TokenVerifier.Result.Correct -> verificationResult.userId
            TokenVerifier.Result.Incorrect -> {
                call.respond(BadRequest, "Invalid token")
                return@get
            }
        }
        when (val result = getUserRoomsUseCase(userId)) {
            GetUserRoomsUseCase.Result.Empty -> {
                call.respond(OK, emptyArray<Unit>())
                return@get
            }

            is GetUserRoomsUseCase.Result.Successful -> {
                call.respond(OK, result.rooms)
                return@get
            }

            GetUserRoomsUseCase.Result.UserNotExists -> {
                call.respond(NotFound, "User not exists")
                return@get
            }
        }
    }
}
