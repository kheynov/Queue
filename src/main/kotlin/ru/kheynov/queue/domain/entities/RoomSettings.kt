package ru.kheynov.queue.domain.entities

import kotlinx.serialization.Serializable

@Serializable
data class RoomSettings(
    val isAlarmed: Boolean,
)