package ru.kheynov.queue.domain.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Room(
    val id: Int,
    val pass: String,
    val name: String,
    @SerialName("users_id") val usersId: List<Int>,
    @SerialName("admins_id") val adminsId: List<Int>,
    val settings: RoomSettings?,
    val announcements: List<Announcement>?,
    val queues: List<Queue>?,
)
