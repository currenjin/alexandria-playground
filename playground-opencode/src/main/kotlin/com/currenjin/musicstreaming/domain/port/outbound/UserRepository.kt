package com.currenjin.musicstreaming.domain.port.outbound

import com.currenjin.musicstreaming.domain.model.User
import com.currenjin.musicstreaming.domain.model.UserId

interface UserRepository {
    fun findById(id: UserId): User?
    fun findByEmail(email: String): User?
    fun save(user: User): User
}
