package com.currenjin.mafia.domain.game

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class GameTest {
    @Test
    fun `빈 게임에 플레이어를 추가하면 목록에 포함된다`() {
        val game = Game.create(maxPlayers = 4)

        val joined = game.join("p1", "player1")

        assertEquals(1, joined.players.size)
        assertTrue(joined.players.any { it.id == "p1" && it.nickname == "player1" })
    }

    @Test
    fun `정원 초과로는 입장할 수 없다`() {
        val game = Game.create(maxPlayers = 2)
            .join("p1", "player1")
            .join("p2", "player2")

        assertThrows(GameError.RoomIsFull::class.java) {
            game.join("p3", "player3")
        }

        assertEquals(2, game.players.size)
    }

    @Test
    fun `게임을 시작하면 상태가 진행 중으로 바뀌고 역할이 배정된다`() {
        val game = Game.create(maxPlayers = 3)
            .join("p1", "player1")
            .join("p2", "player2")
            .join("p3", "player3")

        val started = game.start()

        assertEquals(GamePhase.IN_PROGRESS, started.phase)
        assertTrue(started.players.all { it.role != Role.UNASSIGNED })
        assertTrue(started.players.count { it.role == Role.MAFIA } >= 1)
    }
}