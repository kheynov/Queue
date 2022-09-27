package ru.kheynov.queue.domain.use_cases.rooms

import ru.kheynov.queue.domain.entities.Room
import ru.kheynov.queue.domain.entities.RoomSettings
import ru.kheynov.queue.domain.repositories.RoomsRepository
import ru.kheynov.queue.domain.repositories.UserRepository

class CreateRoomUseCase(
    private val userRepository: UserRepository,
    private val roomsRepository: RoomsRepository,
) {
    sealed interface Result {
        object Successful : Result
        object UserNotExists : Result
        object Failed : Result
    }

    suspend operator fun invoke(
        userId: Int,
        roomName: String,
        password: String,
        settings: RoomSettings? = null,
    ): Result {
        if (!userRepository.checkIfUserRegistered(userId)) return Result.UserNotExists
        val id = roomsRepository.getLastRoomId() + 1
        val room = Room(
            id = id,
            pass = password,
            name = roomName,
            userIds = listOf(userId),
            adminIds = listOf(userId),
            settings = settings,
            announcements = null,
            queues = null,
        )
        return if (roomsRepository.createRoom(room)) Result.Successful else Result.Failed
    }
}