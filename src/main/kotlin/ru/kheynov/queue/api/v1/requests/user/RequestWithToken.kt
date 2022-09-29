package ru.kheynov.queue.api.v1.requests.user

import kotlinx.serialization.Serializable

@Serializable
data class RequestWithToken(
    val token: String,
)