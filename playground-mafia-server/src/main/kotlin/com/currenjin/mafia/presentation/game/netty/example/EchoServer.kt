package com.currenjin.mafia.presentation.game.netty.example

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.LineBasedFrameDecoder
import io.netty.handler.codec.string.StringDecoder
import io.netty.handler.codec.string.StringEncoder

fun main() {
    val bossGroup: EventLoopGroup = NioEventLoopGroup(1)
    val workerGroup: EventLoopGroup = NioEventLoopGroup()

    try {
        val bootstrap = ServerBootstrap()
        bootstrap
            .group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel::class.java)
            .childOption(ChannelOption.SO_KEEPALIVE, true)
            .childHandler(
                object : ChannelInitializer<SocketChannel>() {
                    override fun initChannel(ch: SocketChannel) {
                        ch
                            .pipeline()
                            .addLast(LineBasedFrameDecoder(1024))
                            .addLast(StringDecoder(Charsets.UTF_8))
                            .addLast(StringEncoder(Charsets.UTF_8))
                            .addLast(EchoServerHandler())
                    }
                },
            )

        val channel = bootstrap.bind(9000).sync().channel()
        println("Echo serverv started on port 9000")

        channel.closeFuture().sync()
    } finally {
        bossGroup.shutdownGracefully()
        workerGroup.shutdownGracefully()
    }
}
