package ru.kheynov.queue.domain.use_cases.queues

import ru.kheynov.queue.domain.repositories.RoomsRepository
import ru.kheynov.queue.domain.repositories.UserRepository

class DeleteQueueUseCase(
    private val userRepository: UserRepository,
    private val roomsRepository: RoomsRepository,
) {
    sealed interface Result {
        object Successful : Result
        object Failed : Result
        object UserNotExists : Result
        object RoomNotExists : Result
        object QueueNotExists : Result
        object Forbidden : Result
    }

    suspend operator fun invoke(userId: Int, roomId: Long, queueId: Int): Result {
        if (!userRepository.checkIfUserRegistered(userId)) return Result.UserNotExists
        val room = roomsRepository.getRoomById(roomId) ?: return Result.RoomNotExists
        room.queues?.find { it.id == queueId } ?: return Result.QueueNotExists
        if (!room.adminIds.contains(userId)) return Result.Forbidden
        return if (roomsRepository.deleteQueueById(roomId, queueId)) Result.Successful else Result.Failed
    }
}