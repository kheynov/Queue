package ru.kheynov.queue.domain.use_cases.users

import ru.kheynov.queue.domain.entities.User
import ru.kheynov.queue.domain.repositories.UserRepository

class RegisterUserUseCase(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(user: User): Boolean {
        return userRepository.registerUser(user)
    }
}