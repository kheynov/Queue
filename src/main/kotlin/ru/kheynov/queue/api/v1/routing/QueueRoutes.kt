package ru.kheynov.queue.api.v1.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.kheynov.queue.api.v1.requests.queue.*
import ru.kheynov.queue.di.ServiceLocator
import ru.kheynov.queue.domain.use_cases.UseCases
import ru.kheynov.queue.domain.use_cases.queues.*
import ru.kheynov.queue.security.TokenVerifier

fun Route.queueRoutes(useCases: UseCases) {
    route("/queue") {
        createQueue(useCases.createQueueUseCase)
        deleteQueue(useCases.deleteQueueUseCase)
        getQueueDetails(useCases.getQueueDetailsUseCase)
        joinQueue(useCases.joinQueueUseCase)
        leaveQueue(useCases.leaveQueueUseCase)
    }
}

private fun Route.createQueue(
    createQueueUseCase: CreateQueueUseCase,
    tokenVerifier: TokenVerifier = ServiceLocator.tokenVerifier,
) {
    post {
        val request = call.receiveNullable<CreateQueueRequest>() ?: run {
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

        when (createQueueUseCase(userId, request.roomId, request.queueId, request.name)) {
            CreateQueueUseCase.Result.Failed -> {
                call.respond(HttpStatusCode.Conflict, "Cannot create queue")
                return@post
            }

            CreateQueueUseCase.Result.QueueAlreadyExists -> {
                call.respond(HttpStatusCode.Conflict, "Queue already exists")
                return@post
            }

            CreateQueueUseCase.Result.RoomNotExists -> {
                call.respond(HttpStatusCode.NotFound, "Room not exists")
                return@post
            }

            CreateQueueUseCase.Result.Successful -> {
                call.respond(HttpStatusCode.OK)
                return@post
            }

            CreateQueueUseCase.Result.UserNotExists -> {
                call.respond(HttpStatusCode.NotFound, "User not exists")
                return@post
            }
        }
    }
}

private fun Route.deleteQueue(
    deleteQueueUseCase: DeleteQueueUseCase,
    tokenVerifier: TokenVerifier = ServiceLocator.tokenVerifier,
) {
    delete {
        val request = call.receiveNullable<DeleteQueueRequest>() ?: run {
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
        when (deleteQueueUseCase(userId, request.roomId, request.queueId)) {
            DeleteQueueUseCase.Result.Failed -> {
                call.respond(HttpStatusCode.Conflict, "Cannot delete queue")
                return@delete
            }

            DeleteQueueUseCase.Result.Forbidden -> {
                call.respond(HttpStatusCode.Forbidden)
                return@delete
            }

            DeleteQueueUseCase.Result.QueueNotExists -> {
                call.respond(HttpStatusCode.NotFound, "Queue not exists")
                return@delete
            }

            DeleteQueueUseCase.Result.RoomNotExists -> {
                call.respond(HttpStatusCode.NotFound, "Room not exists")
                return@delete
            }

            DeleteQueueUseCase.Result.Successful -> {
                call.respond(HttpStatusCode.OK)
                return@delete
            }

            DeleteQueueUseCase.Result.UserNotExists -> {
                call.respond(HttpStatusCode.NotFound, "User not exists")
                return@delete
            }
        }
    }
}

private fun Route.getQueueDetails(
    getQueueDetailsUseCase: GetQueueDetailsUseCase,
    tokenVerifier: TokenVerifier = ServiceLocator.tokenVerifier,
) {
    get {
        val request = call.receiveNullable<GetQueueDetailsRequest>() ?: run {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val userId = when (val verificationResult = tokenVerifier.verifyToken(request.token)) {
            is TokenVerifier.Result.Correct -> verificationResult.userId
            TokenVerifier.Result.Incorrect -> {
                call.respond(HttpStatusCode.BadRequest, "Invalid token")
                return@get
            }
        }
        when (val result = getQueueDetailsUseCase(userId, request.roomId, request.queueId)) {
            GetQueueDetailsUseCase.Result.Failed -> {
                call.respond(HttpStatusCode.Conflict, "Failed to get details")
                return@get
            }

            GetQueueDetailsUseCase.Result.Forbidden -> {
                call.respond(HttpStatusCode.Forbidden)
                return@get
            }

            GetQueueDetailsUseCase.Result.QueueNotExists -> {
                call.respond(HttpStatusCode.NotFound, "Queue not exists")
                return@get
            }

            GetQueueDetailsUseCase.Result.RoomNotExists -> {
                call.respond(HttpStatusCode.NotFound, "Room not exists")
                return@get
            }

            is GetQueueDetailsUseCase.Result.Successful -> {
                call.respond(HttpStatusCode.OK, result.queueDetails)
                return@get
            }

            GetQueueDetailsUseCase.Result.UserNotExists -> {
                call.respond(HttpStatusCode.NotFound, "User not exists")
                return@get
            }
        }
    }
}

private fun Route.joinQueue(
    joinQueueUseCase: JoinQueueUseCase,
    tokenVerifier: TokenVerifier = ServiceLocator.tokenVerifier,
) {
    post("/join") {
        val request = call.receiveNullable<JoinQueueRequest>() ?: run {
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
        when (joinQueueUseCase(userId, request.roomId, request.queueId)) {
            JoinQueueUseCase.Result.Failed -> {
                call.respond(HttpStatusCode.Conflict, "Cannot join queue")
                return@post
            }

            JoinQueueUseCase.Result.Forbidden -> {
                call.respond(HttpStatusCode.Forbidden)
                return@post
            }

            JoinQueueUseCase.Result.QueueNotExists -> {
                call.respond(HttpStatusCode.NotFound, "Queue not exists")
                return@post
            }

            JoinQueueUseCase.Result.RoomNotExists -> {
                call.respond(HttpStatusCode.NotFound, "Room not exists")
                return@post
            }

            JoinQueueUseCase.Result.Successful -> {
                call.respond(HttpStatusCode.OK)
                return@post
            }

            JoinQueueUseCase.Result.UserNotExists -> {
                call.respond(HttpStatusCode.NotFound, "User not exists")
                return@post
            }
        }
    }
}

private fun Route.leaveQueue(
    leaveQueueUseCase: LeaveQueueUseCase,
    tokenVerifier: TokenVerifier = ServiceLocator.tokenVerifier,
) {
    delete {
        val request = call.receiveNullable<LeaveQueueRequest>() ?: run {
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
        when (leaveQueueUseCase(userId, request.roomId, request.queueId)) {
            LeaveQueueUseCase.Result.Failed -> {
                call.respond(HttpStatusCode.Conflict, "Cannot leave queue")
                return@delete
            }

            LeaveQueueUseCase.Result.Forbidden -> {
                call.respond(HttpStatusCode.Forbidden)
                return@delete
            }

            LeaveQueueUseCase.Result.QueueNotExists -> {
                call.respond(HttpStatusCode.NotFound, "Queue not exists")
                return@delete
            }

            LeaveQueueUseCase.Result.RoomNotExists -> {
                call.respond(HttpStatusCode.NotFound, "Room not exists")
                return@delete
            }

            LeaveQueueUseCase.Result.UserNotExists -> {
                call.respond(HttpStatusCode.NotFound, "User not exists")
                return@delete
            }

            LeaveQueueUseCase.Result.Successful -> {
                call.respond(HttpStatusCode.OK)
                return@delete
            }
        }

    }
}