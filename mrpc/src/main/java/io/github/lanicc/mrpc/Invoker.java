package io.github.lanicc.mrpc;

import io.github.lanicc.mrpc.remote.NettyClient;
import io.github.lanicc.mrpc.remote.proto.Request;
import io.github.lanicc.mrpc.remote.proto.Response;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

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
            Request request = new Request();
            request.setMethod(method.getName());
            request.setData(args);
            request.setClazz(clazz);
            CompletableFuture<Response> future = nettyClient.requestAsync(request);

            return future.get().getData();
        }

        throw new UnsupportedOperationException(method.getName());
    }
}
