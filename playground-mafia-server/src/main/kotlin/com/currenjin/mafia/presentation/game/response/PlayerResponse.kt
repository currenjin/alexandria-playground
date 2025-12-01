package com.currenjin.mafia.presentation.game.response

import com.currenjin.mafia.domain.game.Player

data class PlayerResponse(
    val id: String,
    val nickname: String,
    val role: String,
    val alive: Boolean,
) {
    companion object {
        fun from(player: Player) =
            PlayerResponse(
                id = player.id,
                nickname = player.nickname,
                role = player.role.name,
                alive = player.alive,
            )
    }
}