package ru.kheynov.queue.domain.use_cases.rooms

import ru.kheynov.queue.domain.repositories.RoomsRepository
import ru.kheynov.queue.domain.repositories.UserRepository

class JoinRoomUseCase(
    private val userRepository: UserRepository,
    private val roomsRepository: RoomsRepository,
) {
    sealed interface Result {
        object Successful : Result
        object AlreadyInRoom : Result
        object WrongPassword : Result
        object RoomNotExists : Result
        object Failed : Result
        object UserNotExists : Result
    }

    suspend operator fun invoke(userId: Int, roomId: Long, password: String): Result {
        if (!userRepository.checkIfUserRegistered(userId)) return Result.UserNotExists
        val room = roomsRepository.getRoomById(roomId) ?: return Result.RoomNotExists
        if (room.pass != password) return Result.WrongPassword
        if (room.userIds.contains(userId)) return Result.AlreadyInRoom
        if (!roomsRepository.addUserToRoom(roomId, userId)) return Result.Failed
        return Result.Successful
    }
}