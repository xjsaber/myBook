package com.xjsaber.netty.pipeline.server;

import com.xjsaber.netty.pipeline.server.inbound.InBoundHandlerA;
import com.xjsaber.netty.pipeline.server.inbound.InBoundHandlerB;
import com.xjsaber.netty.pipeline.server.inbound.InBoundHandlerC;
import com.xjsaber.netty.pipeline.server.outbound.OutBoundHandlerA;
import com.xjsaber.netty.pipeline.server.outbound.OutBoundHandlerB;
import com.xjsaber.netty.pipeline.server.outbound.OutBoundHandlerC;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Date;

/**
 * @author xjsaber
 */
@SuppressWarnings("Duplicates")
public class NettyServer {

    public static void main(String[] args){
        ServerBootstrap bootstrap = new ServerBootstrap();

        // 监听接口
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        // 处理每条连接的数据读写的线程组
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        // 配置两大线程组
        bootstrap.group(bossGroup, workerGroup)
                // 指定IO模型
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline().addLast(new InBoundHandlerA());
                        nioSocketChannel.pipeline().addLast(new InBoundHandlerB());
                        nioSocketChannel.pipeline().addLast(new InBoundHandlerC());

                        nioSocketChannel.pipeline().addLast(new OutBoundHandlerA());
                        nioSocketChannel.pipeline().addLast(new OutBoundHandlerB());
                        nioSocketChannel.pipeline().addLast(new OutBoundHandlerC());

                        nioSocketChannel.pipeline().addLast(new ServerHandler());
                    }
                });
        bind(bootstrap, 8000);
    }

    private static void bind(final ServerBootstrap serverBootstrap, final int port) {
        serverBootstrap.bind(port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println(new Date() + ": 端口[" + port + "]绑定成功!");
            } else {
                System.err.println("端口[" + port + "]绑定失败!");
            }
        });
    }
}
