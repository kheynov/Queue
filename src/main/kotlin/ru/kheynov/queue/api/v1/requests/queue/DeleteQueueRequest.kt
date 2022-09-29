package ru.kheynov.queue.api.v1.requests.queue

import kotlinx.serialization.Serializable

@Serializable
data class DeleteQueueRequest(
    val token: String,
    val roomId: Long,
    val queueId: Int,
)