package com.currenjin.musicstreaming.domain

import com.currenjin.musicstreaming.domain.model.SubscriptionType
import com.currenjin.musicstreaming.domain.model.User
import com.currenjin.musicstreaming.domain.model.UserId
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class UserTest {

    @Test
    fun new_user_is_free_by_default() {
        val user = createUser()

        assertEquals(SubscriptionType.FREE, user.subscription)
        assertFalse(user.isPremium())
    }

    @Test
    fun free_user_cannot_skip_ads() {
        val user = createUser()

        assertFalse(user.canSkipAds())
    }

    @Test
    fun free_user_cannot_download() {
        val user = createUser()

        assertFalse(user.canDownload())
    }

    @Test
    fun upgrade_to_premium_changes_subscription() {
        val user = createUser()

        val premium = user.upgradeToPremium()

        assertEquals(SubscriptionType.PREMIUM, premium.subscription)
        assertTrue(premium.isPremium())
    }

    @Test
    fun premium_user_can_skip_ads() {
        val user = createUser().upgradeToPremium()

        assertTrue(user.canSkipAds())
    }

    @Test
    fun premium_user_can_download() {
        val user = createUser().upgradeToPremium()

        assertTrue(user.canDownload())
    }

    private fun createUser(): User = User(
        id = UserId("user-1"),
        email = "test@example.com",
        displayName = "Test User",
    )
}
