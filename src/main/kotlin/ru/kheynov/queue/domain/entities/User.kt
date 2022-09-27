package ru.kheynov.queue.domain.entities

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class User(
    @BsonId val id: ObjectId = ObjectId(),
    val name: String,
    val vkID: Int,
)

@kotlinx.serialization.Serializable
data class UserDTO(
    val name: String,
    val vkID: Int,
)