package ru.kheynov.queue.domain.repositories

import ru.kheynov.queue.domain.entities.User

interface UserRepository {
    suspend fun registerUser(user: User): Boolean
    suspend fun deleteUser(vkID: Int): Boolean
    suspend fun checkIfUserRegistered(vkID: Int): Boolean
    suspend fun getUserInfo(vkID: Int): User?
}