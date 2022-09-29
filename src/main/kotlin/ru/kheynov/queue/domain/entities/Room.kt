package ru.kheynov.queue.domain.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Room(
    val id: Long,
    val pass: String?,
    val name: String,
    @SerialName("user_ids") val userIds: List<Int>,
    @SerialName("admin_ids") val adminIds: List<Int>,
    val settings: RoomSettings?,
    val announcements: List<Announcement>?,
    val queues: List<Queue>?,
)