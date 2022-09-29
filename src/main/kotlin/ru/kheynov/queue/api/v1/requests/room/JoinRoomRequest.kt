package ru.kheynov.queue.api.v1.requests.room

import kotlinx.serialization.Serializable

@Serializable
data class JoinRoomRequest(
    val token: String,
    val roomId: Long,
    val password: String,
)