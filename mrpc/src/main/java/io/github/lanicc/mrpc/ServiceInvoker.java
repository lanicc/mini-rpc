package io.github.lanicc.mrpc;

import io.github.lanicc.mrpc.remote.proto.Request;
import io.github.lanicc.mrpc.stream.ServerStreamObserver;
import io.netty.channel.Channel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created on 2022/7/11.
 *
 * @author lan
 */
public class ServiceInvoker {

    private final ServerConfig config;


    private final Map<Class<?>, Object> services;

    public ServiceInvoker(ServerConfig config) {
        this.config = config;
        this.services = config.getServices();
    }

    public Object invoke(Request request, Channel channel) {
        Class<?> clazz = request.getClazz();
        Object service = Objects.requireNonNull(services.get(clazz), "no provider");
        try {
            Method method = getMethod(service.getClass(), request.getMethod());
            Object[] args = getArgs(request.getData());
            if (!request.isStream()) {
                return method.invoke(service, args);
            } else {
                return processStreamRequest(request, channel, service, method, args);
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private Object processStreamRequest(Request request, Channel c, Object service, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        args[0] = new ServerStreamObserver<>(request.getRequestId(), c);
        return method.invoke(service, args);
    }


    private Method getMethod(Class<?> c, String name) {
        Optional<Method> optional = Stream.of(c.getDeclaredMethods())
                .filter(method -> Objects.equals(method.getName(), name))
                .findAny();
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new NullPointerException("no such method: " + name);
    }

    private Object[] getArgs(Object data) {
        if (data instanceof Collection) {
            Object[] args = new Object[((Collection<?>) data).size()];
            Iterator<?> iterator = ((Collection<?>) data).iterator();
            int i = 0;
            while (iterator.hasNext()) {
                args[i++] = iterator.next();
            }
            return args;
        }
        return new Object[]{};
    }
}
