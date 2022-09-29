package ru.kheynov.queue.domain.use_cases.queues

import ru.kheynov.queue.domain.repositories.RoomsRepository
import ru.kheynov.queue.domain.repositories.UserRepository

class JoinQueueUseCase(
    private val userRepository: UserRepository,
    private val roomsRepository: RoomsRepository,
) {
    sealed interface Result {
        object Successful : Result
        object Failed : Result
        object UserNotExists : Result
        object RoomNotExists : Result
        object QueueNotExists : Result
        object AlreadyInQueue : Result
        object Forbidden : Result
    }

    suspend operator fun invoke(userId: Int, roomId: Long, queueId: Int): Result {
        if (!userRepository.checkIfUserRegistered(userId)) return Result.UserNotExists
        val room = roomsRepository.getRoomById(roomId) ?: return Result.RoomNotExists
        if (!room.userIds.contains(userId)) return Result.Forbidden
        val queue = room.queues?.find { it.id == queueId } ?: return Result.QueueNotExists
        if (queue.userIds.contains(userId)) return Result.AlreadyInQueue
        if (roomsRepository.addUsersToQueue(roomId, queueId, userId)) return Result.Successful
        return Result.Failed
    }
}