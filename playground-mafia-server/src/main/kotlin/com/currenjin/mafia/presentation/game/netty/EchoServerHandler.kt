package com.currenjin.mafia.presentation.game.netty

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.util.AttributeKey

private val NICKNAME_KEY: AttributeKey<String> = AttributeKey.valueOf("nickname")

class EchoServerHandler : SimpleChannelInboundHandler<String>() {
    override fun channelActive(ctx: ChannelHandlerContext) {
        ctx.writeAndFlush(
            """
            Welcome to Command Server!
            Commands:
              HELP
              PING
              ECHO <message>
              LOGIN <nickname>
              ME
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

        val response =
            when (command) {
                "HELP" -> help()
                "PING" -> "PONG"
                "ECHO" -> echo(parts)
                "LOGIN" -> login(ctx, parts)
                "ME" -> me(ctx)
                "QUIT" -> {
                    ctx.writeAndFlush("Bye!\n")
                    ctx.close()
                    return
                }
                else -> "ERROR Unknown command: $command"
            }

        ctx.writeAndFlush("$response\n")
    }

    private fun help(): String =
        """
        HELP                - show this help
        PING                - pong
        ECHO <message>      - echo message
        LOGIN <nickname>    - set your nickname
        ME                  - show your nickname
        QUIT                - close connection
        """.trimIndent()

    private fun echo(parts: List<String>): String {
        if (parts.size < 2) return "ERROR Usage: ECHO <message>"
        val msg = parts.subList(1, parts.size).joinToString(" ")
        return msg
    }

    private fun login(
        ctx: ChannelHandlerContext,
        parts: List<String>,
    ): String {
        if (parts.size < 2) return "ERROR Usage: LOGIN <nickname>"
        val nickname = parts[1]
        ctx.channel().attr(NICKNAME_KEY).set(nickname)
        return "OK logged in as $nickname"
    }

    private fun me(ctx: ChannelHandlerContext): String {
        val nickname = ctx.channel().attr(NICKNAME_KEY).get()
        return if (nickname == null) {
            "You are not logged in. Use LOGIN <nickname>"
        } else {
            "You are $nickname"
        }
    }
}
