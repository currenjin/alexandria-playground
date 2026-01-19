package com.currenjin.musicstreaming.domain.model

import java.time.Instant

data class User(
    val id: UserId,
    val email: String,
    val displayName: String,
    val subscription: SubscriptionType = SubscriptionType.FREE,
    val createdAt: Instant = Instant.now(),
) {
    fun isPremium(): Boolean = subscription == SubscriptionType.PREMIUM

    fun canSkipAds(): Boolean = isPremium()

    fun canDownload(): Boolean = isPremium()

    fun upgradeToPremium(): User = copy(subscription = SubscriptionType.PREMIUM)
}

@JvmInline
value class UserId(val value: String)

enum class SubscriptionType {
    FREE, PREMIUM
}
