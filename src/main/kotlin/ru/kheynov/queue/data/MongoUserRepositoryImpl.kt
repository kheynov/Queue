package ru.kheynov.queue.data

import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import ru.kheynov.queue.domain.entities.User
import ru.kheynov.queue.domain.repositories.UserRepository

class MongoUserRepositoryImpl(db: CoroutineDatabase) : UserRepository {
    private val users = db.getCollection<User>("users")

    override suspend fun registerUser(user: User): Boolean {
        if (users.findOne(User::vkID eq user.vkID) != null) return false
        return users.insertOne(user).wasAcknowledged()
    }

    override suspend fun deleteUser(vkID: Int): Boolean {
        val user = users.findOne(User::vkID eq vkID) ?: return false
        return users.deleteOne(User::vkID eq user.vkID).wasAcknowledged()
    }

    override suspend fun checkIfUserRegistered(vkID: Int): Boolean {
        return users.findOne(User::vkID eq vkID) != null
    }

    override suspend fun getUserInfo(vkID: Int): User? {
        return users.findOne(User::vkID eq vkID)
    }

}
