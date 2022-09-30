package ru.kheynov.queue.domain.use_cases.rooms

import ru.kheynov.queue.domain.entities.Room
import ru.kheynov.queue.domain.repositories.RoomsRepository
import ru.kheynov.queue.domain.repositories.UserRepository

class GetRoomDetailsUseCase(
    private val userRepository: UserRepository,
    private val roomsRepository: RoomsRepository,
) {
    sealed interface Result {
        data class Successful(val room: Room) : Result
        object UserNotExists : Result
        object RoomNotExists : Result
        object Forbidden : Result
        object Failed : Result
    }

    suspend operator fun invoke(userId: Int, roomId: Long): Result {
        if (!userRepository.checkIfUserRegistered(userId)) return Result.UserNotExists
        var room = roomsRepository.getRoomById(roomId) ?: return Result.RoomNotExists
        if (!room.userIds.contains(userId)) return Result.Forbidden

        if (!room.adminIds.contains(userId)) {
            room = room.copy(
                pass = null
            )
        }
        return Result.Successful(room)
    }

}