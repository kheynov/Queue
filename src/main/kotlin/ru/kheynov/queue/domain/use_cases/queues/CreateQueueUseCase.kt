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
    }

    suspend operator fun invoke(userId: Int, roomId: Long, name: String): Result {
        if (!userRepository.checkIfUserRegistered(userId)) return Result.UserNotExists

        val queue = Queue(
            id = roomsRepository.getLastQueueId(roomId) + 1, name = name, userIds = listOf(userId)
        )
        return if (roomsRepository.createQueue(roomId, queue)) Result.Successful
        else Result.Failed
    }
}