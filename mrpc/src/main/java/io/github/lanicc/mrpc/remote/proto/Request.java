package io.github.lanicc.mrpc.remote.proto;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created on 2022/7/9.
 *
 * @author lan
 */
public class Request extends Protocol {

    private static final AtomicLong REQUEST_ID = new AtomicLong(0);

    private Class<?> clazz;

    private String method;

    public Request() {
        this.requestId = REQUEST_ID.getAndIncrement();
    }


    public Class<?> getClazz() {
        return clazz;
    }

    public Request setClazz(Class<?> clazz) {
        this.clazz = clazz;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public Request setMethod(String method) {
        this.method = method;
        return this;
    }

    @Override
    public String toString() {
        return "Request{" +
                "clazz=" + clazz +
                ", method='" + method + '\'' +
                ", requestId=" + requestId +
                ", data=" + data +
                '}';
    }
}
