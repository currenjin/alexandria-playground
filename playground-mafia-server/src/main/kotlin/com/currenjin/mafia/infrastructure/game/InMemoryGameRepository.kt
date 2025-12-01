package com.currenjin.mafia.infrastructure.game

import com.currenjin.mafia.domain.game.Game
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class InMemoryGameRepository : GameRepository {

    private val storage = ConcurrentHashMap<String, Game>()

    override fun save(game: Game): Game {
        storage[game.id] = game
        return game
    }

    override fun findById(id: String): Game? = storage[id]
}