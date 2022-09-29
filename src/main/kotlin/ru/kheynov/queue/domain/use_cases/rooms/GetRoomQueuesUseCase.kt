package ru.kheynov.queue.domain.use_cases.rooms

import ru.kheynov.queue.domain.entities.QueueThumbnail
import ru.kheynov.queue.domain.repositories.RoomsRepository
import ru.kheynov.queue.domain.repositories.UserRepository

class GetRoomQueuesUseCase(
    private val userRepository: UserRepository,
    private val roomsRepository: RoomsRepository,
) {
    sealed interface Result {
        data class Successful(val queues: List<QueueThumbnail>) : Result
        object UserNotExists : Result
        object RoomNotExists : Result
        object Empty : Result
        object Forbidden : Result
    }

    suspend operator fun invoke(userId: Int, roomId: Long): Result {
        if (!userRepository.checkIfUserRegistered(userId)) return Result.UserNotExists
        val room = roomsRepository.getRoomById(roomId) ?: return Result.RoomNotExists
        if (!room.userIds.contains(userId)) return Result.Forbidden
        val queues = room.queues ?: return Result.Empty
        val result = queues.map { queue ->
            QueueThumbnail(
                id = queue.id,
                name = queue.name,
                usersCount = queue.userIds.size
            )
        }
        return Result.Successful(result)
    }

}