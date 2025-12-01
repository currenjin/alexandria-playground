package com.currenjin.mafia.infrastructure.game

import com.currenjin.mafia.domain.game.Game

interface GameRepository {
    fun save(game: Game): Game
    fun findById(id: String): Game?
}
