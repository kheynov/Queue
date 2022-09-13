package ru.kheynov.queue.domain.entities

import kotlinx.serialization.Serializable

@Serializable
data class Queue(
    val name: String,
    val usersId: List<Int>,
)
