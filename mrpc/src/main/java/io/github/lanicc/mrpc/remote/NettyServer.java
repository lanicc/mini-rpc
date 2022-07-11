package io.github.lanicc.mrpc.remote;

import io.github.lanicc.mrpc.Config;
import io.github.lanicc.mrpc.ServerConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.concurrent.ExecutionException;

/**
 * Created on 2022/7/11.
 *
 * @author lan
 */
public class NettyServer {
    private final ChannelFuture channelFuture;

    private final EventLoopGroup bossGroup;

    private final EventLoopGroup workerGroup;


    public NettyServer(ServerConfig config) {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();
        channelFuture =
                new ServerBootstrap()
                        .group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .handler(new LoggingHandler(LogLevel.DEBUG))
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel channel) throws Exception {
                                channel.pipeline()
                                        .addLast(new LengthFieldBasedFrameDecoder(1024 * 1024 * 1024, 0, 4, 0, 4))
                                        .addLast(new LengthFieldPrepender(4))
                                        .addLast(new ProtoCodec(config.getSerializer()))
                                        .addLast(new ServerHandler(config));
                            }
                        })
                        .bind(config.getServerSocketAddr());
    }

    public void start() {
        try {
            channelFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

}
