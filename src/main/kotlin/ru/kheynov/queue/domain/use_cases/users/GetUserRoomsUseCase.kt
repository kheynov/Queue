package ru.kheynov.queue.domain.use_cases.users

import ru.kheynov.queue.domain.entities.Room
import ru.kheynov.queue.domain.repositories.RoomsRepository
import ru.kheynov.queue.domain.repositories.UserRepository

class GetUserRoomsUseCase(
    private val userRepository: UserRepository,
    private val roomsRepository: RoomsRepository,
) {
    sealed interface Result {
        data class Successful(val rooms: List<Room>) : Result
        object UserNotExists : Result
        object Empty : Result
    }

    suspend operator fun invoke(userId: Int): Result {
        if (!userRepository.checkIfUserRegistered(userId)) return Result.UserNotExists
        val rooms = roomsRepository.getUserRooms(userId).toMutableList()
        if (rooms.isEmpty()) return Result.Empty
        rooms.forEach { room ->
            if (!room.adminIds.contains(userId)) {
                rooms[rooms.indexOf(room)] = room.copy(
                    pass = Array(room.pass.length) { "*" }.joinToString("")
                )
            }
        }
        return Result.Successful(rooms)
    }
}