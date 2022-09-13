package ru.kheynov.queue.domain.entities

import kotlinx.serialization.Serializable

@Serializable
data class Room(
    val id: Int,
    val pass: String,
    val name: String,
    val usersId: List<Int>,
    val adminsId: List<Int>,
    val settings: RoomSettings?,
    val announcements: List<Announcement>?,
    val queues: List<Queue>?,
)
