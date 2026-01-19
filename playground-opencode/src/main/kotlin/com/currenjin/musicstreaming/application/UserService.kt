package com.currenjin.musicstreaming.application

import com.currenjin.musicstreaming.domain.model.MusicStreamingError
import com.currenjin.musicstreaming.domain.model.SubscriptionType
import com.currenjin.musicstreaming.domain.model.User
import com.currenjin.musicstreaming.domain.model.UserId
import com.currenjin.musicstreaming.domain.port.inbound.UserUseCase
import com.currenjin.musicstreaming.domain.port.outbound.UserRepository
import java.util.UUID

class UserService(
    private val userRepository: UserRepository,
) : UserUseCase {

    override fun getUser(userId: UserId): User? {
        return userRepository.findById(userId)
    }

    override fun createUser(email: String, displayName: String): User {
        userRepository.findByEmail(email)?.let {
            throw MusicStreamingError.InvalidOperation("User with email $email already exists")
        }

        val user = User(
            id = UserId(UUID.randomUUID().toString()),
            email = email,
            displayName = displayName,
            subscription = SubscriptionType.FREE,
        )

        return userRepository.save(user)
    }

    override fun upgradeToPremium(userId: UserId): User {
        val user = userRepository.findById(userId)
            ?: throw MusicStreamingError.UserNotFound(userId)

        if (user.isPremium()) {
            throw MusicStreamingError.InvalidOperation("User is already premium")
        }

        return userRepository.save(user.upgradeToPremium())
    }

    override fun updateDisplayName(userId: UserId, displayName: String): User {
        val user = userRepository.findById(userId)
            ?: throw MusicStreamingError.UserNotFound(userId)

        return userRepository.save(user.copy(displayName = displayName))
    }
}
