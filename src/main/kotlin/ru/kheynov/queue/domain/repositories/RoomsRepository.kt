package ru.kheynov.queue.domain.repositories

import ru.kheynov.queue.domain.entities.Queue
import ru.kheynov.queue.domain.entities.Room

interface RoomsRepository {
    suspend fun createRoom(room: Room): Boolean
    suspend fun getRoomById(id: Long): Room?
    suspend fun deleteRoomById(id: Long): Boolean

    suspend fun addUserToRoom(roomId: Long, vararg userIds: Int): Boolean
    suspend fun deleteUserFromRoom(userId: Int, roomId: Long): Boolean
    suspend fun addUsersToAdmins(roomId: Long, vararg userIds: Int): Boolean
    suspend fun getUserRooms(userId: Int): List<Room>

    suspend fun createQueue(roomId: Long, queue: Queue): Boolean
    suspend fun deleteQueueById(roomId: Long, queueId: Int): Boolean
    suspend fun getRoomQueues(roomId: Long): List<Queue>?
    suspend fun addUsersToQueue(roomId: Long, queueId: Int, vararg userIds: Int): Boolean
    suspend fun deleteUserFromQueue(roomId: Long, queueId: Int, userId: Int): Boolean

    suspend fun getLastRoomId(): Long
    suspend fun getLastQueueId(roomId: Long): Int
}