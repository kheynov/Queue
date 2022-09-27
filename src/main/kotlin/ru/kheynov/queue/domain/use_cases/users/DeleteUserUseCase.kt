package ru.kheynov.queue.domain.use_cases.users

import ru.kheynov.queue.domain.repositories.UserRepository

class DeleteUserUseCase(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(vkId: Int): Boolean {
        return userRepository.deleteUser(vkId)
    }
}