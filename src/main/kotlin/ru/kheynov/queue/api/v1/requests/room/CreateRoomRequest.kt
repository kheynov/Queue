package ru.kheynov.queue.api.v1.requests.room

import kotlinx.serialization.Serializable
import ru.kheynov.queue.domain.entities.RoomSettings

@Serializable
data class CreateRoomRequest(
    val token: String,
    val roomName: String,
    val password: String,
    val settings: RoomSettings? = null,
)