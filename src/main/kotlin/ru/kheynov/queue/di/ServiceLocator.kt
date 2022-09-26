package ru.kheynov.queue.di

import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import ru.kheynov.queue.data.MongoRoomsRepositoryImpl
import ru.kheynov.queue.data.MongoUserRepositoryImpl
import ru.kheynov.queue.domain.use_cases.UseCases


object ServiceLocator {
    private val db = KMongo.createClient(
        connectionString = System.getenv("MONGO_CONNECTION_STRING")
    ).coroutine.getDatabase("queues")

    private val roomsRepository = MongoRoomsRepositoryImpl(db)

    private val userRepository = MongoUserRepositoryImpl(db)

    val useCases = UseCases(
        userRepository = userRepository,
        roomsRepository = roomsRepository
    )
}