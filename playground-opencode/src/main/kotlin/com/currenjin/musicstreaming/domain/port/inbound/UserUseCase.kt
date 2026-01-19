package com.currenjin.musicstreaming.domain.port.inbound

import com.currenjin.musicstreaming.domain.model.User
import com.currenjin.musicstreaming.domain.model.UserId

interface UserUseCase {
    fun getUser(userId: UserId): User?
    fun createUser(email: String, displayName: String): User
    fun upgradeToPremium(userId: UserId): User
    fun updateDisplayName(userId: UserId, displayName: String): User
}
