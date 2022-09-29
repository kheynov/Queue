package ru.kheynov.queue.api.v1.requests.room

import kotlinx.serialization.Serializable

@Serializable
data class GetRoomQueues(
    val token: String,
    val roomId: Long,
)