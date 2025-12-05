package com.currenjin.mafia.presentation.game.netty.game

import com.currenjin.mafia.domain.game.GameError
import com.currenjin.mafia.service.game.GameService
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler

class GameServerHandler(
    private val gameService: GameService,
) : SimpleChannelInboundHandler<String>() {
    override fun channelActive(ctx: ChannelHandlerContext) {
        ctx.writeAndFlush(
            """
            Welcome to Mafia Game Server!
            Commands:
              CREATE <maxPlayers>
              JOIN <gameId> <playerId> <nickname>
              START <gameId>
              VOTE <gameId> <voterId> <targetId>
              NEXT <gameId>
              STATE <gameId>
              QUIT
            
            """.trimIndent() + "\n",
        )
    }

    override fun channelRead0(
        ctx: ChannelHandlerContext,
        msg: String,
    ) {
        val line = msg.trim()
        if (line.isBlank()) return

        val parts = line.split(" ")
        val command = parts[0].uppercase()

        try {
            val response =
                when (command) {
                    "CREATE" -> handleCreate(parts)
                    "JOIN" -> handleJoin(parts)
                    "START" -> handleStart(parts)
                    "VOTE" -> handleVote(parts)
                    "NEXT" -> handleNext(parts)
                    "STATE" -> handleState(parts)
                    "QUIT" -> {
                        ctx.writeAndFlush("Bye!\n")
                        ctx.close()
                        return
                    }
                    else -> "ERROR Unknown command: $command"
                }

            ctx.writeAndFlush("$response\n")
        } catch (e: IllegalArgumentException) {
            ctx.writeAndFlush("ERROR ${e.message}\n")
        } catch (e: GameError) {
            ctx.writeAndFlush("ERROR ${e.message}\n")
        } catch (e: Exception) {
            ctx.writeAndFlush("ERROR ${e.javaClass.simpleName}: ${e.message}\n")
        }
    }

    private fun handleCreate(parts: List<String>): String {
        if (parts.size < 2) return "ERROR Usage: CREATE <maxPlayers>"
        val maxPlayers = parts[1].toInt()
        val game = gameService.createGame(maxPlayers)
        return "GAME_CREATED id=${game.id} maxPlayers=${game.maxPlayers}"
    }

    private fun handleJoin(parts: List<String>): String {
        if (parts.size < 4) return "ERROR Usage: JOIN <gameId> <playerId> <nickname>"
        val gameId = parts[1]
        val playerId = parts[2]
        val nickname = parts.subList(3, parts.size).joinToString(" ")

        val game = gameService.joinGame(gameId, playerId, nickname)
        return "JOINED gameId=${game.id} players=${game.players.size}"
    }

    private fun handleStart(parts: List<String>): String {
        if (parts.size < 2) return "ERROR Usage: START <gameId>"
        val gameId = parts[1]
        val game = gameService.startGame(gameId)
        return "STARTED gameId=${game.id} phase=${game.phase} turn=${game.turn}"
    }

    private fun handleVote(parts: List<String>): String {
        if (parts.size < 4) return "ERROR Usage: VOTE <gameId> <voterId> <targetId>"
        val gameId = parts[1]
        val voterId = parts[2]
        val targetId = parts[3]

        val game = gameService.vote(gameId, voterId, targetId)
        return "VOTED gameId=${game.id} votes=${game.votes.size}"
    }

    private fun handleNext(parts: List<String>): String {
        if (parts.size < 2) return "ERROR Usage: NEXT <gameId>"
        val gameId = parts[1]
        val game = gameService.nextTurn(gameId)
        return "NEXT gameId=${game.id} turn=${game.turn} turnNumber=${game.turnNumber}"
    }

    private fun handleState(parts: List<String>): String {
        if (parts.size < 2) return "ERROR Usage: STATE <gameId>"
        val gameId = parts[1]
        val game = gameService.getGame(gameId)

        val playersDesc =
            game.players.joinToString(",") { p ->
                "${p.id}:${p.nickname}:${p.role}:${if (p.alive) "alive" else "dead"}"
            }

        return "STATE gameId=${game.id} phase=${game.phase} turn=${game.turn} turnNumber=${game.turnNumber} players=[$playersDesc]"
    }
}
