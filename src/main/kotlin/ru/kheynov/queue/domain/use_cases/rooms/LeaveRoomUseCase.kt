package ru.kheynov.queue.domain.use_cases.rooms

import ru.kheynov.queue.domain.repositories.RoomsRepository
import ru.kheynov.queue.domain.repositories.UserRepository

class LeaveRoomUseCase(
    private val userRepository: UserRepository,
    private val roomsRepository: RoomsRepository,
) {
    operator fun invoke() {
        TODO()
    }
}