package ru.kheynov.queue.domain.use_cases.rooms

import ru.kheynov.queue.domain.repositories.RoomsRepository
import ru.kheynov.queue.domain.repositories.UserRepository

class DeleteRoomUseCase(
    private val userRepository: UserRepository,
    private val roomsRepository: RoomsRepository,
) {
    sealed interface Result {
        object Successful : Result
        object Forbidden : Result
        object Failed : Result
    }

    suspend operator fun invoke(userId: Int, roomId: Long): Result {
        if (!userRepository.checkIfUserRegistered(userId)) return Result.Forbidden
        val room = roomsRepository.getRoomById(roomId) ?: return Result.Failed
        if (!room.adminIds.contains(userId)) return Result.Forbidden
        return if (roomsRepository.deleteRoomById(roomId)) Result.Successful else Result.Failed
    }
}