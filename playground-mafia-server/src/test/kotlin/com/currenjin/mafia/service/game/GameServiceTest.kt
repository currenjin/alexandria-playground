package com.currenjin.mafia.service.game

import com.currenjin.mafia.domain.game.Game
import com.currenjin.mafia.domain.game.GameError
import com.currenjin.mafia.domain.game.GamePhase
import com.currenjin.mafia.domain.game.Turn
import com.currenjin.mafia.infrastructure.game.GameRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GameServiceTest {
    private lateinit var gameRepository: FakeGameRepository
    private lateinit var gameService: GameService

    @BeforeEach
    fun setUp() {
        gameRepository = FakeGameRepository()
        gameService = GameService(gameRepository)
    }

    @Test
    fun `createGame - 새 게임을 만들고 저장한다`() {
        val game = gameService.createGame(maxPlayers = 4)

        assertNotNull(game.id)
        assertEquals(4, game.maxPlayers)
        assertEquals(GamePhase.WAITING, game.phase)

        val loaded = gameRepository.findById(game.id)
        assertNotNull(loaded)
        assertEquals(game.id, loaded!!.id)
    }

    @Test
    fun `joinGame - 존재하는 게임에 플레이어를 추가한다`() {
        val game = gameService.createGame(maxPlayers = 2)

        val updated =
            gameService.joinGame(
                gameId = game.id,
                playerId = "p1",
                nickname = "player1",
            )

        assertEquals(1, updated.players.size)
        assertTrue(updated.players.any { it.id == "p1" && it.nickname == "player1" })

        val reloaded = gameRepository.findById(game.id)
        assertEquals(1, reloaded!!.players.size)
    }

    @Test
    fun `joinGame - 존재하지 않는 게임이면 예외를 던진다`() {
        val ex =
            assertThrows(IllegalArgumentException::class.java) {
                gameService.joinGame(
                    gameId = "not-exist",
                    playerId = "p1",
                    nickname = "player1",
                )
            }
        assertEquals("Game not found", ex.message)
    }

    @Test
    fun `joinGame - 이미 꽉 찬 게임이면 도메인 예외를 그대로 전파한다`() {
        val game = gameService.createGame(maxPlayers = 1)
        gameService.joinGame(game.id, "p1", "player1")

        assertThrows(GameError.RoomIsFull::class.java) {
            gameService.joinGame(
                gameId = game.id,
                playerId = "p2",
                nickname = "player2",
            )
        }
    }

    @Test
    fun `startGame - WAITING 상태의 게임을 시작하고 역할을 부여한다`() {
        val game = gameService.createGame(maxPlayers = 3)
        gameService.joinGame(game.id, "p1", "player1")
        gameService.joinGame(game.id, "p2", "player2")
        gameService.joinGame(game.id, "p3", "player3")

        val started = gameService.startGame(game.id)

        assertEquals(GamePhase.IN_PROGRESS, started.phase)
        assertTrue(started.players.all { it.role.name != "UNASSIGNED" })

        val reloaded = gameRepository.findById(game.id)
        assertEquals(GamePhase.IN_PROGRESS, reloaded!!.phase)
    }

    @Test
    fun `startGame - 존재하지 않는 게임이면 예외를 던진다`() {
        val ex =
            assertThrows(IllegalArgumentException::class.java) {
                gameService.startGame("not-exist")
            }
        assertEquals("Game not found", ex.message)
    }

    @Test
    fun `vote - 서비스 레벨에서 투표를 위임하고 저장한다`() {
        val game = gameService.createGame(maxPlayers = 3)
        gameService.joinGame(game.id, "p1", "player1")
        gameService.joinGame(game.id, "p2", "player2")
        gameService.joinGame(game.id, "p3", "player3")
        gameService.startGame(game.id)

        val afterVote =
            gameService.vote(
                gameId = game.id,
                voterId = "p1",
                targetId = "p2",
            )

        assertEquals("p2", afterVote.votes["p1"])
        val reloaded = gameRepository.findById(game.id)
        assertEquals("p2", reloaded!!.votes["p1"])
    }

    @Test
    fun `nextTurn - 서비스 레벨에서 턴 전환과 결과 반영을 처리한다`() {
        val game = gameService.createGame(maxPlayers = 3)
        gameService.joinGame(game.id, "p1", "player1")
        gameService.joinGame(game.id, "p2", "player2")
        gameService.joinGame(game.id, "p3", "player3")
        gameService.startGame(game.id)

        gameService.vote(game.id, "p1", "p2")
        gameService.vote(game.id, "p3", "p2")

        val afterTurn = gameService.nextTurn(game.id)

        assertEquals(Turn.NIGHT, afterTurn.turn)
        val target = afterTurn.players.first { it.id == "p2" }
        assertFalse(target.alive)

        val reloaded = gameRepository.findById(game.id)!!
        assertEquals(Turn.NIGHT, reloaded.turn)
        assertTrue(reloaded.votes.isEmpty())
    }

    @Test
    fun `getGame - 게임을 조회한다`() {
        val game = gameService.createGame(maxPlayers = 3)

        val target = gameService.getGame(game.id)

        assertEquals(game.id, target.id)
    }

    @Test
    fun `getGame - 게임이 존재하지 않으면 예외를 전파한다`() {
        assertThrows(IllegalArgumentException::class.java) {
            gameService.getGame(gameId = "nothing")
        }
    }
}

/**
 * 테스트용 인메모리 구현.
 * 실제 InMemoryGameRepository를 써도 되지만,
 * 테스트 안에서 로직을 완전히 통제하기 위해 별도 Fake를 사용.
 */
private class FakeGameRepository : GameRepository {
    private val storage = mutableMapOf<String, Game>()

    override fun save(game: Game): Game {
        storage[game.id] = game
        return game
    }

    override fun findById(id: String): Game? = storage[id]
}
