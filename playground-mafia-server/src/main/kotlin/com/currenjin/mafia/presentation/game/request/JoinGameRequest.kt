package com.currenjin.mafia.presentation.game.request

data class JoinGameRequest(
    val playerId: String,
    val nickname: String,
)