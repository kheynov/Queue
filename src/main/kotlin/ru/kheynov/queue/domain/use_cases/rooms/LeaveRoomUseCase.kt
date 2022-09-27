package ru.kheynov.queue.domain.use_cases.rooms

import ru.kheynov.queue.domain.repositories.RoomsRepository
import ru.kheynov.queue.domain.repositories.UserRepository

class LeaveRoomUseCase(
    private val userRepository: UserRepository,
    private val roomsRepository: RoomsRepository,
) {
    sealed interface Result {
        object Successful : Result
        object UserNotExists : Result
        object RoomNotExists : Result
        object Failed : Result
    }

    suspend operator fun invoke(userId: Int, roomId: Long): Result {
        if (!userRepository.checkIfUserRegistered(userId)) return Result.UserNotExists
        roomsRepository.getRoomById(roomId) ?: return Result.RoomNotExists
        return if (roomsRepository.deleteUserFromRoom(userId, roomId)) Result.Successful else Result.Failed
    }
}