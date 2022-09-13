package ru.kheynov.queue.domain.entities

import kotlinx.serialization.Serializable

@Serializable
data class Announcement(
    val title: String,
    val text: String,
)
