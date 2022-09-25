package ru.kheynov.queue.domain.repositories

import ru.kheynov.queue.domain.entities.Queue
import ru.kheynov.queue.domain.entities.Room

interface RoomsRepository {
    suspend fun createRoom(room: Room): Boolean
    suspend fun getRoomById(id: Int): Room?
    suspend fun deleteRoomById(id: Int): Boolean

    suspend fun addUserToRoom(roomId: Int, vararg userIds: Int): Boolean
    suspend fun deleteUserFromRoom(userId: Int, roomId: Int): Boolean
    suspend fun addUsersToAdmins(roomId: Int, vararg userIds: Int): Boolean
    suspend fun getUserRooms(userId: Int): List<Room>

    suspend fun createQueue(roomId: Int, queue: Queue): Boolean
    suspend fun deleteQueueById(roomId: Int, queueName: String): Boolean
    suspend fun getRoomQueues(roomId: Int): List<Queue>?
}