package io.github.lanicc.mrpc.remote;

import io.github.lanicc.mrpc.remote.proto.Protocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.internal.PlatformDependent;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2022/7/11.
 *
 * @author lan
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ClientHandler extends SimpleChannelInboundHandler<Protocol> {

    private final Map<Long, CompletableFuture> completableFutureMap;

    private final Semaphore throttle;

    private final int maxWaitSize;

    public ClientHandler(int maxWaitSize) {
        this.maxWaitSize = maxWaitSize;
        this.completableFutureMap = PlatformDependent.newConcurrentHashMap(maxWaitSize);
        this.throttle = new Semaphore(maxWaitSize);
    }

    public <T> CompletableFuture<T> future(long requestId) throws InterruptedException {
        boolean b = throttle.tryAcquire(1, TimeUnit.SECONDS);
        if (!b) {
            throw new RuntimeException("too many waiting requests");
        }
        CompletableFuture<T> completableFuture = new CompletableFuture<>();
        completableFuture.whenComplete((t, throwable) -> remove(requestId));
        CompletableFuture<T> future = completableFutureMap.putIfAbsent(requestId, completableFuture);
        return Objects.isNull(future) ? completableFuture : future;
    }

    public void remove(long id) {
        throttle.release();
        completableFutureMap.remove(id);
    }

    public int getProcessingRequestCount() {
        return maxWaitSize - throttle.availablePermits();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Protocol protocol) throws Exception {
        long requestId = protocol.getRequestId();
        CompletableFuture future = completableFutureMap.remove(requestId);
        future.complete(protocol);
        throttle.release();
    }
}
