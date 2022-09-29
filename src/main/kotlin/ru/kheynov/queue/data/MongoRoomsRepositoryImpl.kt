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

    override suspend fun getRoomById(id: Long): Room? {
        return rooms.findOne(Room::id eq id)
    }

    override suspend fun deleteRoomById(id: Long): Boolean {
        if (rooms.findOne(Room::id eq id) == null) return false
        return rooms.deleteOne(Room::id eq id).wasAcknowledged()
    }

    override suspend fun addUserToRoom(roomId: Long, vararg userIds: Int): Boolean {
        if (rooms.findOne(Room::id eq roomId) == null) return false
        val usersList = rooms.findOne(Room::id eq roomId)?.userIds?.toMutableList() ?: return false
        usersList.addAll(userIds.toList())
        return rooms.updateOne(Room::id eq roomId, setValue(Room::userIds, usersList)).wasAcknowledged()
    }

    override suspend fun deleteUserFromRoom(userId: Int, roomId: Long): Boolean {
        if (rooms.findOne(Room::id eq roomId) == null) return false
        val usersList = rooms.findOne(Room::id eq roomId)?.userIds?.toMutableList() ?: return false
        var isSuccessful = usersList.remove(userId)
        if (isSuccessful) {
            isSuccessful = rooms.updateOne(Room::id eq roomId, setValue(Room::userIds, usersList)).wasAcknowledged()
        }
        return isSuccessful
    }

    override suspend fun addUsersToAdmins(roomId: Long, vararg userIds: Int): Boolean {
        if (rooms.findOne(Room::id eq roomId) == null) return false
        val usersList = rooms.findOne(Room::id eq roomId)?.adminIds?.toMutableList() ?: return false
        usersList.addAll(userIds.toList())
        return rooms.updateOne(Room::id eq roomId, setValue(Room::userIds, usersList)).wasAcknowledged()
    }

    override suspend fun getUserRooms(userId: Int): List<Room> {
        return rooms.find(Room::userIds contains userId).toList()
    }

    override suspend fun createQueue(roomId: Long, queue: Queue): Boolean {
        val room = rooms.findOne(Room::id eq roomId) ?: return false
        val queuesList = room.queues?.toMutableList() ?: mutableListOf()
        queuesList.add(queue)
        return rooms.updateOne(Room::id eq roomId, setValue(Room::queues, queuesList)).wasAcknowledged()
    }

    override suspend fun deleteQueueById(roomId: Long, queueId: Int): Boolean {
        if (rooms.findOne(Room::id eq roomId) == null) return false
        val queues = rooms.findOne(Room::id eq roomId)?.queues?.toMutableList() ?: return false
        var isSuccessful = queues.remove(queues.find { it.id == queueId })
        if (isSuccessful) {
            isSuccessful = rooms.updateOne(Room::id eq roomId, setValue(Room::queues, queues)).wasAcknowledged()
        }
        return isSuccessful
    }

    override suspend fun getRoomQueues(roomId: Long): List<Queue>? {
        return rooms.find(Room::id eq roomId).first()?.queues
    }

    override suspend fun addUsersToQueue(roomId: Long, queueId: Int, vararg userIds: Int): Boolean {
        val queues = rooms.findOne(Room::id eq roomId)?.queues?.toMutableList() ?: return false
        val queue = queues.find { it.id == queueId }
        queues.remove(queue)
        val queueUsers = queue?.userIds?.toMutableList()
        queueUsers?.addAll(userIds.toList())
        queues.add(queue?.copy(userIds = queueUsers?.toList() ?: return false) ?: return false)
        return rooms.updateOne(Room::id eq roomId, setValue(Room::queues, queues)).wasAcknowledged()
    }

    override suspend fun deleteUserFromQueue(roomId: Long, queueId: Int, userId: Int): Boolean {
        val queues = rooms.findOne(Room::id eq roomId)?.queues?.toMutableList() ?: return false
        val queue = queues.find { it.id == queueId }
        queues.remove(queue)
        val queueUsers = queue?.userIds?.toMutableList()
        queueUsers?.remove(userId)
        queues.add(queue?.copy(userIds = queueUsers?.toList() ?: return false) ?: return false)
        return rooms.updateOne(Room::id eq roomId, setValue(Room::queues, queues)).wasAcknowledged()
    }

    override suspend fun getLastRoomId(): Long {
        return rooms.countDocuments()
    }

    override suspend fun getLastQueueId(roomId: Long): Int {
        val queues = rooms.findOne(Room::id eq roomId)?.queues?.toMutableList() ?: return 0
        return queues.lastIndex
    }
}