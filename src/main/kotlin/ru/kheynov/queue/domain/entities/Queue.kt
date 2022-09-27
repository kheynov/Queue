package ru.kheynov.queue.domain.entities

import kotlinx.serialization.Serializable

@Serializable
data class Queue(
    val id: Int,
    val name: String,
    val userIds: List<Int>,
)

data class QueueDetails(
    val id: Int,
    val name: String,
    val users: List<UserDTO>,
)

data class QueueThumbnail(
    val id: Int,
    val name: String,
    val usersCount: Int,
)
