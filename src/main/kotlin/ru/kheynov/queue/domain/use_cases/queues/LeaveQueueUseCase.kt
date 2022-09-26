package ru.kheynov.queue.domain.use_cases.queues

import ru.kheynov.queue.domain.repositories.RoomsRepository
import ru.kheynov.queue.domain.repositories.UserRepository

class LeaveQueueUseCase(
    private val userRepository: UserRepository,
    private val roomsRepository: RoomsRepository,
) {//TODO
}