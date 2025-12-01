package com.currenjin.mafia.domain.game

import java.util.UUID

data class Game(
    val id: String,
    val maxPlayers: Int,
    val players: List<Player>,
    val phase: GamePhase,
    val turn: Turn,
    val turnNumber: Int,
    val votes: Map<String, String>,
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

    fun vote(voterId: String, targetId: String): Game {
        if (phase != GamePhase.IN_PROGRESS) {
            throw GameError.InvalidPhase("진행 중인 게임에서만 투표할 수 있습니다.")
        }
        if (turn != Turn.DAY) {
            throw GameError.InvalidTurn("DAY 턴에만 투표할 수 있습니다.")
        }

        val voter = players.find { it.id == voterId } ?: throw GameError.UnknownPlayer()
        val target = players.find { it.id == targetId } ?: throw GameError.UnknownPlayer()

        if (!voter.alive) {
            throw GameError.DeadPlayer("사망한 플레이어는 투표할 수 없습니다.")
        }
        if (!target.alive) {
            throw GameError.InvalidTarget("사망한 플레이어에게는 투표할 수 없습니다.")
        }

        val updatedVotes = votes + (voterId to targetId)
        return copy(votes = updatedVotes)
    }

    fun nextTurn(): Game {
        if (phase != GamePhase.IN_PROGRESS) {
            throw GameError.InvalidPhase("IN_PROGRESS 상태에서만 턴을 넘길 수 있습니다.")
        }

        val updated =
            when (turn) {
                Turn.DAY -> resolveDayVotes()
                Turn.NIGHT -> copy(
                    turn = Turn.DAY,
                    votes = emptyMap(),
                )
            }

        return updated.copy(turnNumber = turnNumber + 1)
    }

    private fun resolveDayVotes(): Game {
        if (votes.isEmpty()) {
            return copy(
                turn = Turn.NIGHT,
                votes = emptyMap(),
            )
        }

        val counted =
            votes.values.groupingBy { it }.eachCount()

        val maxCount = counted.values.maxOrNull() ?: 0
        val topTargets = counted.filterValues { it == maxCount }.keys

        val updatedPlayers =
            if (topTargets.size == 1) {
                val targetId = topTargets.first()
                players.map { p ->
                    if (p.id == targetId) {
                        p.copy(alive = false)
                    } else {
                        p
                    }
                }
            } else {
                players
            }

        return copy(
            players = updatedPlayers,
            turn = Turn.NIGHT,
            votes = emptyMap(),
        )
    }

    companion object {
        fun create(maxPlayers: Int): Game =
            Game(
                id = UUID.randomUUID().toString(),
                maxPlayers = maxPlayers,
                players = emptyList(),
                phase = GamePhase.WAITING,
                turn = Turn.DAY,
                turnNumber = 0,
                votes = emptyMap(),
            )
    }
}