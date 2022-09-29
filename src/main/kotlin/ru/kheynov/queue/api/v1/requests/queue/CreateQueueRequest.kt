package ru.kheynov.queue.api.v1.requests.queue

import kotlinx.serialization.Serializable

@Serializable
data class CreateQueueRequest(
    val token: String,
    val roomId: Long,
    val name: String,
)