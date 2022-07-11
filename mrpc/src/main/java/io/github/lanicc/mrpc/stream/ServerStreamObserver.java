package io.github.lanicc.mrpc.stream;

import io.netty.channel.Channel;

/**
 * Created on 2022/7/11.
 *
 * @author lan
 */
public class ServerStreamObserver<T> implements StreamObserver<T> {

    private final long requestId;

    private final Channel channel;

    public ServerStreamObserver(long requestId, Channel channel) {
        this.requestId = requestId;
        this.channel = channel;
    }

    @Override
    public void onNext(T t) {
        channel.writeAndFlush(new StreamResponseMessage<>(requestId, t, false));
    }

    @Override
    public void onCompleted() {
        channel.writeAndFlush(new StreamResponseMessage<>(requestId, null, true));
    }
}
