package ru.kheynov.queue.domain.use_cases.users

import ru.kheynov.queue.domain.repositories.RoomsRepository
import ru.kheynov.queue.domain.repositories.UserRepository
import ru.kheynov.queue.domain.use_cases.rooms.DeleteRoomUseCase

class DeleteUserUseCase(
    private val userRepository: UserRepository,
    private val roomsRepository: RoomsRepository,
) {
    suspend operator fun invoke(vkId: Int): Boolean {
        try {
            val rooms = roomsRepository.getUserRooms(vkId)
            rooms.forEach { room ->
                if (room.adminIds.contains(vkId) && room.adminIds.size == 1) {
                    DeleteRoomUseCase(userRepository, roomsRepository).invoke(userId = vkId, room.id)
                }
            }
        } catch (e: Exception) {
            println(e.message)
        }
        return userRepository.deleteUser(vkId)
    }
}