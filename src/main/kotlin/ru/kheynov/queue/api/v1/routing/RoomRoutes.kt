package ru.kheynov.queue.api.v1.routing

import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.Conflict
import io.ktor.http.HttpStatusCode.Companion.NotFound
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.http.HttpStatusCode.Companion.Unauthorized
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.kheynov.queue.api.v1.requests.room.*
import ru.kheynov.queue.di.ServiceLocator
import ru.kheynov.queue.domain.use_cases.UseCases
import ru.kheynov.queue.domain.use_cases.rooms.*
import ru.kheynov.queue.security.TokenVerifier

fun Route.roomRoutes(
    useCases: UseCases,
) {
    route("/room") {
        createRoom(useCases.createRoomUseCase)
        deleteRoom(useCases.deleteRoomUseCase)
        getRoomQueues(useCases.getRoomQueuesUseCase)
        joinRoom(useCases.joinRoomsUseCase)
        leaveRoom(useCases.leaveRoomUseCase)
    }
}

private fun Route.createRoom(
    createRoomUseCase: CreateRoomUseCase,
    tokenVerifier: TokenVerifier = ServiceLocator.tokenVerifier,
) {
    post {
        val request = call.receiveNullable<CreateRoomRequest>() ?: run {
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
        when (createRoomUseCase(userId, request.roomName, request.password, request.settings)) {
            CreateRoomUseCase.Result.Failed -> {
                call.respond(Conflict, "Room already exists")
                return@post
            }

            CreateRoomUseCase.Result.Successful -> {
                call.respond(HttpStatusCode.Created)
                return@post
            }

            CreateRoomUseCase.Result.UserNotExists -> {
                call.respond(BadRequest, "User not exists")
                return@post
            }
        }
    }
}

private fun Route.deleteRoom(
    deleteRoomUseCase: DeleteRoomUseCase,
    tokenVerifier: TokenVerifier = ServiceLocator.tokenVerifier,
) {
    delete {
        val request = call.receiveNullable<DeleteRoomRequest>() ?: run {
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
        when (deleteRoomUseCase(userId, request.roomId)) {
            DeleteRoomUseCase.Result.Failed -> {
                call.respond(Conflict, "Cannot delete room")
                return@delete
            }

            DeleteRoomUseCase.Result.Forbidden -> {
                call.respond(HttpStatusCode.Forbidden)
                return@delete
            }

            DeleteRoomUseCase.Result.Successful -> {
                call.respond(OK)
                return@delete
            }
        }
    }
}

private fun Route.getRoomQueues(
    getRoomQueuesUseCase: GetRoomQueuesUseCase,
    tokenVerifier: TokenVerifier = ServiceLocator.tokenVerifier,
) {
    get("/queues") {
        val request = call.receiveNullable<GetRoomQueues>() ?: run {
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
        when (val queues = getRoomQueuesUseCase(userId, request.roomId)) {
            GetRoomQueuesUseCase.Result.Forbidden -> {
                call.respond(HttpStatusCode.Forbidden)
                return@get
            }

            GetRoomQueuesUseCase.Result.Empty -> {
                call.respond(HttpStatusCode.NoContent, "Empty")
                return@get
            }

            GetRoomQueuesUseCase.Result.RoomNotExists -> {
                call.respond(NotFound, "Room not exists")
                return@get
            }

            is GetRoomQueuesUseCase.Result.Successful -> {
                call.respond(OK, queues)
                return@get
            }

            GetRoomQueuesUseCase.Result.UserNotExists -> {
                call.respond(NotFound, "User Not Exists")
                return@get
            }
        }
    }
}

private fun Route.joinRoom(
    joinRoomUseCase: JoinRoomUseCase,
    tokenVerifier: TokenVerifier = ServiceLocator.tokenVerifier,
) {
    post("/join") {
        val request = call.receiveNullable<JoinRoomRequest>() ?: run {
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
        when (joinRoomUseCase(userId, request.roomId, request.password)) {
            JoinRoomUseCase.Result.AlreadyInRoom -> {
                call.respond(Conflict, "Already in room")
                return@post
            }

            JoinRoomUseCase.Result.Failed -> {
                call.respond(Conflict, "Failed to join")
                return@post
            }

            JoinRoomUseCase.Result.RoomNotExists -> {
                call.respond(NotFound, "Room not exists")
                return@post
            }

            JoinRoomUseCase.Result.Successful -> {
                call.respond(OK)
                return@post
            }

            JoinRoomUseCase.Result.UserNotExists -> {
                call.respond(NotFound, "User not exists")
                return@post
            }

            JoinRoomUseCase.Result.WrongPassword -> {
                call.respond(Unauthorized, "Wrong password")
                return@post
            }
        }
    }
}

private fun Route.leaveRoom(
    leaveRoomUseCase: LeaveRoomUseCase,
    tokenVerifier: TokenVerifier = ServiceLocator.tokenVerifier,
) {
    delete("/leave") {
        val request = call.receiveNullable<LeaveRoomRequest>() ?: run {
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
        when (leaveRoomUseCase(userId, request.roomId)) {
            LeaveRoomUseCase.Result.Failed -> {
                call.respond(Conflict, "Cannot leave the room")
                return@delete
            }

            LeaveRoomUseCase.Result.RoomNotExists -> {
                call.respond(NotFound, "Room not exists")
                return@delete
            }

            LeaveRoomUseCase.Result.Successful -> {
                call.respond(OK)
                return@delete
            }

            LeaveRoomUseCase.Result.UserNotExists -> {
                call.respond(NotFound, "User not exists")
                return@delete
            }
        }
    }
}


