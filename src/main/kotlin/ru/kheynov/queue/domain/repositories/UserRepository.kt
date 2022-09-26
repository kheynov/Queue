package ru.kheynov.queue.domain.repositories

import ru.kheynov.queue.domain.entities.User

interface UserRepository {
    suspend fun registerUser(user: User)
    suspend fun deleteUser(vkID: Int)
    suspend fun checkIfUserRegistered(vkID: Int)
}