package ru.kheynov.queue.api.v1.requests

import kotlinx.serialization.Serializable

@Serializable
data class GetRoomQueues(
    val token: String,
    val roomId: Long,
)