package ru.kheynov.queue.domain.use_cases.rooms

import ru.kheynov.queue.domain.entities.Room
import ru.kheynov.queue.domain.repositories.RoomsRepository
import ru.kheynov.queue.domain.repositories.UserRepository
import java.util.ListResourceBundle

class GetUserRoomsUseCase(
    private val userRepository: UserRepository,
    private val roomsRepository: RoomsRepository,
) {
    operator fun invoke(): List<Room> {
        TODO()
    }
}