package com.currenjin.mafia.service.game

import com.currenjin.mafia.domain.game.Game
import com.currenjin.mafia.infrastructure.game.GameRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GameService(
    private val gameRepository: GameRepository,
) {

    @Transactional
    fun createGame(maxPlayers: Int): Game {
        val game = Game.create(maxPlayers)
        return gameRepository.save(game)
    }

    @Transactional
    fun joinGame(gameId: String, playerId: String, nickname: String): Game {
        val game =
            gameRepository.findById(gameId)
                ?: throw IllegalArgumentException("Game not found")

        val updated = game.join(playerId, nickname)
        return gameRepository.save(updated)
    }

    @Transactional
    fun startGame(gameId: String): Game {
        val game =
            gameRepository.findById(gameId)
                ?: throw IllegalArgumentException("Game not found")

        val started = game.start()
        return gameRepository.save(started)
    }
}
