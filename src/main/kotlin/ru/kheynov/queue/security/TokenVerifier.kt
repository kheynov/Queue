package ru.kheynov.queue.security

import kotlin.math.absoluteValue

class TokenVerifier {
    sealed interface Result {
        data class Correct(val userId: Int) : Result
        object Incorrect : Result
    }

    fun verifyToken(token: String): Result {
        //TODO: add VK token verification
        return Result.Correct(token.hashCode().absoluteValue)
    }
}