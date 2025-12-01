package com.currenjin.mafia.presentation.game.response

import com.currenjin.mafia.domain.game.Game
import com.currenjin.mafia.presentation.game.response.PlayerResponse

data class GameResponse(
    val id: String,
    val maxPlayers: Int,
    val phase: String,
    val players: List<PlayerResponse>,
) {
    companion object {
        fun from(game: Game) =
            GameResponse(
                id = game.id,
                maxPlayers = game.maxPlayers,
                phase = game.phase.name,
                players = game.players.map { PlayerResponse.Companion.from(it) },
            )
    }
}