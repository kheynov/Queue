package ru.kheynov.queue.domain.use_cases.queues

import ru.kheynov.queue.domain.entities.QueueDetails
import ru.kheynov.queue.domain.entities.UserDTO
import ru.kheynov.queue.domain.repositories.RoomsRepository
import ru.kheynov.queue.domain.repositories.UserRepository

class GetQueueDetailsUseCase(
    private val userRepository: UserRepository,
    private val roomsRepository: RoomsRepository,
) {
    sealed interface Result {
        data class Successful(val queueDetails: QueueDetails) : Result
        object UserNotExists : Result
        object RoomNotExists : Result
        object QueueNotExists : Result
        object Forbidden : Result
        object Failed : Result
    }

    suspend operator fun invoke(userId: Int, roomId: Long, queueId: Int): Result {
        if (!userRepository.checkIfUserRegistered(userId)) return Result.UserNotExists
        val room = roomsRepository.getRoomById(roomId) ?: return Result.RoomNotExists
        val queue = room.queues?.find { it.id == queueId } ?: return Result.QueueNotExists
        if (!room.userIds.contains(userId)) return Result.Forbidden

        val queueDetails = QueueDetails(
            id = queue.id,
            name = queue.name,
            users = queue.userIds.map {
                val user = userRepository.getUserInfo(userId)
                UserDTO(
                    user?.name ?: return Result.Failed,
                    userId
                )
            }
        )
        return Result.Successful(queueDetails)
    }
}