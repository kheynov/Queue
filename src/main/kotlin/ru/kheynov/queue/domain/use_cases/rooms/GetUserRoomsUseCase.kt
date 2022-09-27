package ru.kheynov.queue.domain.use_cases.rooms

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
        val rooms = roomsRepository.getUserRooms(userId)
        if (rooms.isEmpty()) return Result.Empty
        return Result.Successful(rooms)
    }
}