package ru.kheynov.queue.domain.use_cases

import ru.kheynov.queue.domain.repositories.RoomsRepository
import ru.kheynov.queue.domain.repositories.UserRepository
import ru.kheynov.queue.domain.use_cases.queues.*
import ru.kheynov.queue.domain.use_cases.rooms.*
import ru.kheynov.queue.domain.use_cases.users.DeleteUserUseCase
import ru.kheynov.queue.domain.use_cases.users.RegisterUserUseCase

class UseCases(
    userRepository: UserRepository,
    roomsRepository: RoomsRepository,
) {
    val registerUserUseCase = RegisterUserUseCase(userRepository)
    val deleteUserUseCase = DeleteUserUseCase(userRepository)

    val createRoomUseCase = CreateRoomUseCase(userRepository, roomsRepository)
    val deleteRoomUseCase = DeleteRoomUseCase(userRepository, roomsRepository)
    val getUserRoomsUseCase = GetUserRoomsUseCase(userRepository, roomsRepository)
    val joinRoomsUseCase = JoinRoomUseCase(userRepository, roomsRepository)
    val leaveRoomUseCase = LeaveRoomUseCase(userRepository, roomsRepository)

    val createQueueUseCase = CreateQueueUseCase(userRepository, roomsRepository)
    val deleteQueueUseCase = DeleteQueueUseCase(userRepository, roomsRepository)
    val getQueueDetailsUseCase = GetQueueDetailsUseCase(userRepository, roomsRepository)
    val getRoomQueuesUseCase = GetRoomQueuesUseCase(userRepository, roomsRepository)
    val joinQueueUseCase = JoinQueueUseCase(userRepository, roomsRepository)
    val leaveQueueUseCase = LeaveQueueUseCase(userRepository, roomsRepository)
}
