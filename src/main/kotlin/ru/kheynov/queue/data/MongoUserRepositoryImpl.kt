package ru.kheynov.queue.data

import org.litote.kmongo.coroutine.CoroutineDatabase
import ru.kheynov.queue.domain.entities.User
import ru.kheynov.queue.domain.repositories.UserRepository

class MongoUserRepositoryImpl(db: CoroutineDatabase) : UserRepository {
    override suspend fun registerUser(user: User) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(vkID: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun checkIfUserRegistered(vkID: Int) {
        TODO("Not yet implemented")
    }

}
