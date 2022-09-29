package ru.kheynov.queue.api.v1.requests.queue

import kotlinx.serialization.Serializable

@Serializable
data class LeaveQueueRequest(
    val token: String,
    val roomId: Long,
    val queueId: Int,
)