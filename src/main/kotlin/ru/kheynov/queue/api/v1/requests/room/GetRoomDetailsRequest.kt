package ru.kheynov.queue.api.v1.requests.room

import kotlinx.serialization.Serializable

@Serializable
data class GetRoomDetailsRequest(
    val token: String,
    val roomId: Long,
)
