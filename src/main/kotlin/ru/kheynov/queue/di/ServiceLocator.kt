package ru.kheynov.queue.di

import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import ru.kheynov.queue.data.MongoRoomsRepositoryImpl


object ServiceLocator {
    private val db = KMongo.createClient(
        connectionString = System.getenv("MONGO_CONNECTION_STRING")
    ).coroutine.getDatabase("queues")

    val roomsRepository = MongoRoomsRepositoryImpl(db)
}