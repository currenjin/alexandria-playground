package com.currenjin.mafia.presentation.game.netty

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler

class EchoServerHandler : SimpleChannelInboundHandler<String>() {
    override fun channelActive(ctx: ChannelHandlerContext) {
        ctx.writeAndFlush("Welcome to Echo Server!\nType anything and I will echo it.\n\n")
    }

    override fun channelRead0(ctx: ChannelHandlerContext, msg: String) {
        val line = msg.trim()
        if (line.equals("quit", ignoreCase = true)) {
            ctx.writeAndFlush("Bye!\n")
            ctx.close()
            return
        }

        ctx.writeAndFlush("ECHO: $line\n")
    }
}
