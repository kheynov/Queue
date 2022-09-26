package ru.kheynov.queue.domain.repositories

import ru.kheynov.queue.domain.entities.User

interface UserRepository {
    suspend fun registerUser(user: User): Boolean
    suspend fun deleteUser(vkID: String): Boolean
    suspend fun checkIfUserRegistered(vkID: String): Boolean
}