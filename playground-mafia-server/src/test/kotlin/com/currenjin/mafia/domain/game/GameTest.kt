package com.currenjin.mafia.domain.game

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
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

    @Test
    fun `DAY 턴에 살아있는 플레이어가 다른 플레이어에게 투표할 수 있다`() {
        val game =
            Game.create(maxPlayers = 3)
                .join("p1", "player1")
                .join("p2", "player2")
                .join("p3", "player3")
                .start() // 시작하면 IN_PROGRESS, 기본 DAY 1이라고 가정

        val voted = game.vote(voterId = "p1", targetId = "p2")

        assertEquals(1, voted.votes.size)
        assertEquals("p2", voted.votes["p1"])
    }

    @Test
    fun `NIGHT 턴에는 투표할 수 없다`() {
        val game =
            Game.create(maxPlayers = 3)
                .join("p1", "player1")
                .join("p2", "player2")
                .join("p3", "player3")
                .start()
                .nextTurn()

        val ex =
            assertThrows(GameError.InvalidTurn::class.java) {
                game.vote("p1", "p2")
            }

        assertTrue(ex.message!!.contains("DAY 턴에만"))
    }

    @Test
    fun `투표 종료시 최다 득표자는 사망하고 턴이 NIGHT로 전환된다`() {
        val game =
            Game.create(maxPlayers = 3)
                .join("p1", "player1")
                .join("p2", "player2")
                .join("p3", "player3")
                .start()
                .vote("p1", "p2")
                .vote("p3", "p2")
                .vote("p2", "p1")

        val afterResolve = game.nextTurn()

        val target = afterResolve.players.first { it.id == "p2" }
        assertFalse(target.alive)
        assertEquals(Turn.NIGHT, afterResolve.turn)
        assertEquals(1, afterResolve.turnNumber)
        assertTrue(afterResolve.votes.isEmpty())
    }

    @Test
    fun `동점이면 아무도 죽지 않고 턴만 전환된다`() {
        val game =
            Game.create(maxPlayers = 3)
                .join("p1", "player1")
                .join("p2", "player2")
                .join("p3", "player3")
                .start()
                .vote("p1", "p2")
                .vote("p3", "p1")

        val afterResolve = game.nextTurn()

        assertTrue(afterResolve.players.first { it.id == "p1" }.alive)
        assertTrue(afterResolve.players.first { it.id == "p2" }.alive)
        assertEquals(Turn.NIGHT, afterResolve.turn)
    }
}