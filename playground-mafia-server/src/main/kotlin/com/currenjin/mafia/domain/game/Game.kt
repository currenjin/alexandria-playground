package com.currenjin.mafia.domain.game

import java.util.UUID

data class Game(
    val id: String,
    val maxPlayers: Int,
    val players: List<Player>,
    val phase: GamePhase,
) {

    fun join(playerId: String, nickname: String): Game {
        if (players.size >= maxPlayers) {
            throw GameError.RoomIsFull()
        }
        if (phase != GamePhase.WAITING) {
            throw GameError.AlreadyStarted()
        }
        if (players.any { it.id == playerId }) {
            throw GameError.DuplicatedPlayer()
        }

        val newPlayer = Player(
            id = playerId,
            nickname = nickname,
        )

        return copy(players = players + newPlayer)
    }

    fun start(): Game {
        if (phase != GamePhase.WAITING) {
            throw GameError.InvalidPhase("게임은 WAITING 상태에서만 시작할 수 있습니다.")
        }
        if (players.size < 3) {
            throw GameError.NotEnoughPlayers()
        }

        val shuffled = players.shuffled()
        val mafiaCount = maxOf(1, players.size / 3)

        val assigned =
            shuffled.mapIndexed { index, p ->
                if (index < mafiaCount) {
                    p.copy(role = Role.MAFIA)
                } else {
                    p.copy(role = Role.CITIZEN)
                }
            }

        return copy(
            players = assigned,
            phase = GamePhase.IN_PROGRESS,
        )
    }

    companion object {
        fun create(maxPlayers: Int): Game =
            Game(
                id = UUID.randomUUID().toString(),
                maxPlayers = maxPlayers,
                players = emptyList(),
                phase = GamePhase.WAITING,
            )
    }
}