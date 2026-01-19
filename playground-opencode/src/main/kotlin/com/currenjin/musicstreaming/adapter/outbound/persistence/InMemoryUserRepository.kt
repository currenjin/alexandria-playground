package com.currenjin.musicstreaming.adapter.outbound.persistence

import com.currenjin.musicstreaming.domain.model.User
import com.currenjin.musicstreaming.domain.model.UserId
import com.currenjin.musicstreaming.domain.port.outbound.UserRepository

class InMemoryUserRepository : UserRepository {
    private val users = mutableMapOf<UserId, User>()

    override fun findById(id: UserId): User? = users[id]

    override fun findByEmail(email: String): User? =
        users.values.find { it.email == email }

    override fun save(user: User): User {
        users[user.id] = user
        return user
    }
}
