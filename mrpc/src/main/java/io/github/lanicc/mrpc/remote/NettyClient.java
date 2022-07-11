package io.github.lanicc.mrpc.remote;

import io.github.lanicc.mrpc.Config;
import io.github.lanicc.mrpc.remote.proto.Request;
import io.github.lanicc.mrpc.remote.proto.Response;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created on 2022/7/9.
 *
 * @author lan
 */
public class NettyClient {

    private final Config config;

    private Channel channel;

    private ClientHandler clientHandler;

    private ChannelFuture connectFuture;

    private EventLoopGroup group;

    private ProtoCodec protoCodec;

    public NettyClient(Config config) {
        this.config = config;
        init();
    }

    private void init() {
        group = new NioEventLoopGroup();

        clientHandler = new ClientHandler(20);
        protoCodec = new ProtoCodec(config.getSerializer());
        connectFuture =
                new Bootstrap()
                        .channel(NioSocketChannel.class)
                        .option(ChannelOption.TCP_NODELAY, true)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel channel) throws Exception {
                                channel.pipeline()
                                        .addLast(new LengthFieldBasedFrameDecoder(1024 * 1024 * 1024, 0, 4, 0, 4))
                                        .addLast(new LengthFieldPrepender(4))
                                        .addLast(protoCodec)
                                        .addLast(clientHandler);
                            }

                        })
                        .group(group)
                        .connect(config.getServerSocketAddr());
    }

    public void start() {
        try {
            channel = connectFuture.sync().channel();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void shutdown() {
        group.shutdownGracefully();
    }

    public CompletableFuture<Response> requestAsync(Request request) throws InterruptedException {
        CompletableFuture<Response> future = clientHandler.future(request.getRequestId());
        channel.writeAndFlush(request);
        return future;
    }

    public Response requestSync(Request request) throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<Response> future = requestAsync(request);
        Response response = future.get(3, TimeUnit.SECONDS);
        if (future.isDone()) {
            return response;
        }
        future.cancel(false);
        throw new TimeoutException();
    }
}
