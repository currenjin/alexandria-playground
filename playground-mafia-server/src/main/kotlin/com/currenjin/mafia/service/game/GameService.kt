package com.currenjin.mafia.service.game

import com.currenjin.mafia.domain.game.Game
import com.currenjin.mafia.infrastructure.game.GameRepository
import org.springframework.stereotype.Service

@Service
class GameService(
    private val gameRepository: GameRepository,
) {
    fun createGame(maxPlayers: Int): Game {
        val game = Game.create(maxPlayers)
        return gameRepository.save(game)
    }

    fun joinGame(
        gameId: String,
        playerId: String,
        nickname: String,
    ): Game {
        val game =
            gameRepository.findById(gameId)
                ?: throw IllegalArgumentException("Game not found")

        val updated = game.join(playerId, nickname)
        return gameRepository.save(updated)
    }

    fun startGame(gameId: String): Game {
        val game =
            gameRepository.findById(gameId)
                ?: throw IllegalArgumentException("Game not found")

        val started = game.start()
        return gameRepository.save(started)
    }

    fun vote(
        gameId: String,
        voterId: String,
        targetId: String,
    ): Game {
        val game =
            gameRepository.findById(gameId)
                ?: throw IllegalArgumentException("Game not found")

        val updated = game.vote(voterId, targetId)
        return gameRepository.save(updated)
    }

    fun nextTurn(gameId: String): Game {
        val game =
            gameRepository.findById(gameId)
                ?: throw IllegalArgumentException("Game not found")

        val updated = game.nextTurn()
        return gameRepository.save(updated)
    }
}
