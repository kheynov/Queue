package ru.kheynov.queue.domain.use_cases.queues

import ru.kheynov.queue.domain.entities.Queue
import ru.kheynov.queue.domain.repositories.RoomsRepository
import ru.kheynov.queue.domain.repositories.UserRepository

class CreateQueueUseCase(
    private val userRepository: UserRepository,
    private val roomsRepository: RoomsRepository,
) {

    sealed interface Result {
        object Successful : Result
        object Failed : Result
        object UserNotExists : Result
        object RoomNotExists : Result
        object QueueAlreadyExists : Result
    }

    suspend operator fun invoke(userId: Int, roomId: Long, queueId: Int, name: String): Result {
        if (!userRepository.checkIfUserRegistered(userId)) return Result.UserNotExists
        val room = roomsRepository.getRoomById(roomId) ?: return Result.RoomNotExists
        if (room.queues?.find { it.id == queueId } != null) return Result.QueueAlreadyExists

        val queue = Queue(
            id = roomsRepository.getLastQueueId(roomId) + 1,
            name = name,
            userIds = listOf(userId)
        )
        if (roomsRepository.createQueue(roomId, queue)) return Result.Successful

        return Result.Failed
    }
}