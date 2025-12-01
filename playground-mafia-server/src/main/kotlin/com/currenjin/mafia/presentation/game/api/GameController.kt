package com.currenjin.mafia.presentation.game.api

import com.currenjin.mafia.presentation.game.request.CreateGameRequest
import com.currenjin.mafia.presentation.game.request.JoinGameRequest
import com.currenjin.mafia.presentation.game.response.GameResponse
import com.currenjin.mafia.service.game.GameService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/games")
class GameController(
    private val gameService: GameService,
) {

    @PostMapping
    fun createGame(@RequestBody request: CreateGameRequest): ResponseEntity<GameResponse> {
        val game = gameService.createGame(request.maxPlayers)
        return ResponseEntity.ok(GameResponse.from(game))
    }

    @PostMapping("/{gameId}/join")
    fun joinGame(
        @PathVariable gameId: String,
        @RequestBody request: JoinGameRequest,
    ): ResponseEntity<GameResponse> {
        val game = gameService.joinGame(gameId, request.playerId, request.nickname)
        return ResponseEntity.ok(GameResponse.from(game))
    }

    @PostMapping("/{gameId}/start")
    fun startGame(
        @PathVariable gameId: String,
    ): ResponseEntity<GameResponse> {
        val game = gameService.startGame(gameId)
        return ResponseEntity.ok(GameResponse.from(game))
    }
}