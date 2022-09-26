package ru.kheynov.queue.data

import org.litote.kmongo.contains
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.setValue
import ru.kheynov.queue.domain.entities.Queue
import ru.kheynov.queue.domain.entities.Room
import ru.kheynov.queue.domain.repositories.RoomsRepository

class MongoRoomsRepositoryImpl(
    db: CoroutineDatabase,
) : RoomsRepository {
    private val rooms = db.getCollection<Room>("rooms")

    override suspend fun createRoom(room: Room): Boolean {
        if (rooms.findOne(Room::name eq room.name) != null) return false
        return rooms.insertOne(room).wasAcknowledged()
    }

    override suspend fun getRoomById(id: Int): Room? {
        return rooms.findOne(Room::id eq id)
    }

    override suspend fun deleteRoomById(id: Int): Boolean {
        if (rooms.findOne(Room::id eq id) == null) return false
        return rooms.deleteOne(Room::id eq id).wasAcknowledged()
    }

    override suspend fun addUserToRoom(roomId: Int, vararg userIds: Int): Boolean {
        if (rooms.findOne(Room::id eq roomId) == null) return false
        val usersList = rooms.findOne(Room::id eq roomId)?.userIds?.toMutableList() ?: return false
        usersList.addAll(userIds.toList())
        return rooms.updateOne(Room::id eq roomId, setValue(Room::userIds, usersList)).wasAcknowledged()
    }

    override suspend fun deleteUserFromRoom(userId: Int, roomId: Int): Boolean {
        if (rooms.findOne(Room::id eq roomId) == null) return false
        val usersList = rooms.findOne(Room::id eq roomId)?.userIds?.toMutableList() ?: return false
        var isSuccessful = usersList.remove(userId)
        if (isSuccessful) {
            isSuccessful = rooms.updateOne(Room::id eq roomId, setValue(Room::userIds, usersList)).wasAcknowledged()
        }
        return isSuccessful
    }

    override suspend fun addUsersToAdmins(roomId: Int, vararg userIds: Int): Boolean {
        if (rooms.findOne(Room::id eq roomId) == null) return false
        val usersList = rooms.findOne(Room::id eq roomId)?.adminIds?.toMutableList() ?: return false
        usersList.addAll(userIds.toList())
        return rooms.updateOne(Room::id eq roomId, setValue(Room::userIds, usersList)).wasAcknowledged()
    }

    override suspend fun getUserRooms(userId: Int): List<Room> {
        return rooms.find(Room::userIds contains userId).toList()
    }

    override suspend fun createQueue(roomId: Int, queue: Queue): Boolean {
        if (rooms.findOne(Room::id eq roomId) == null) return false
        val queuesList = rooms.findOne(Room::id eq roomId)?.queues?.toMutableList() ?: return false
        queuesList.add(queue)
        return rooms.updateOne(Room::id eq roomId, setValue(Room::queues, queuesList)).wasAcknowledged()
    }

    override suspend fun deleteQueueById(roomId: Int, queueName: String): Boolean {
        if (rooms.findOne(Room::id eq roomId) == null) return false
        val queues = rooms.findOne(Room::id eq roomId)?.queues?.toMutableList() ?: return false
        var isSuccessful = queues.remove(queues.find { it.name == queueName })
        if (isSuccessful) {
            isSuccessful = rooms.updateOne(Room::id eq roomId, setValue(Room::queues, queues)).wasAcknowledged()
        }
        return isSuccessful
    }

    override suspend fun getRoomQueues(roomId: Int): List<Queue>? {
        return rooms.find(Room::id eq roomId).first()?.queues
    }

}