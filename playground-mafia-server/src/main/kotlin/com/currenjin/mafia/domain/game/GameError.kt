package com.currenjin.mafia.domain.game

sealed class GameError(message: String) : RuntimeException(message) {

    class RoomIsFull : GameError("방 정원이 가득 찼습니다.")

    class AlreadyStarted : GameError("이미 시작된 게임입니다.")

    class DuplicatedPlayer : GameError("이미 입장한 플레이어입니다.")

    class NotEnoughPlayers : GameError("플레이어 수가 부족합니다.")

    class InvalidPhase(message: String) : GameError(message)

    class InvalidTurn(message: String) : GameError(message)

    class UnknownPlayer : GameError("게임에 존재하지 않는 플레이어입니다.")

    class DeadPlayer(message: String) : GameError(message)

    class InvalidTarget(message: String) : GameError(message)
}