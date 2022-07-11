package io.github.lanicc.mrpc;

import io.github.lanicc.mrpc.remote.NettyClient;
import io.github.lanicc.mrpc.remote.proto.Request;
import io.github.lanicc.mrpc.remote.proto.Response;
import io.github.lanicc.mrpc.stream.StreamObserver;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2022/7/9.
 *
 * @author lan
 */
public class Invoker implements MethodInterceptor {

    private final Class<?> clazz;

    private final NettyClient nettyClient;

    public Invoker(Class<?> clazz, NettyClient nettyClient) {
        this.clazz = clazz;
        this.nettyClient = nettyClient;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        if (Objects.equals(method.getDeclaringClass(), clazz)) {
            Request request = initRequest(new Request(), method, args);

            if (!request.isStream()) {
                CompletableFuture<Response> future = nettyClient.requestAsync(request);
                return future.get(3, TimeUnit.SECONDS).getData();
            }
            nettyClient.streamRequest(request);
            return null;
        }

        throw new UnsupportedOperationException(method.getName());
    }

    private Request initRequest(Request request, Method method, Object[] args) {
        request.setMethod(method.getName());
        request.setClazz(clazz);
        if (Objects.nonNull(args) && args.length > 0) {
            // 仅支持第一个参数为StreamObserver的流式调用
            if (args[0] instanceof StreamObserver) {
                request.setStreamObserver((StreamObserver<?>) args[0]);
                request.setStream(true);
            }
            if (request.isStream()) {
                Object[] args1 = new Object[args.length];
                System.arraycopy(args, 0, args1, 0, args.length);
                args1[0] = null;
                request.setData(args1);
            } else {
                request.setData(args);
            }
        }

        return request;
    }
}
