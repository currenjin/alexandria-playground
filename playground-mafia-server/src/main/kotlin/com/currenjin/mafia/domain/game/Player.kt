package com.currenjin.mafia.domain.game

data class Player(
    val id: String,
    val nickname: String,
    val role: Role = Role.UNASSIGNED,
    val alive: Boolean = true,
)