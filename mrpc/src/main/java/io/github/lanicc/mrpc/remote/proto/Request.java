package io.github.lanicc.mrpc.remote.proto;

import io.github.lanicc.mrpc.stream.StreamObserver;

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

    private boolean stream;

    private transient StreamObserver<?> streamObserver;
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

    public boolean isStream() {
        return stream;
    }

    public Request setStream(boolean stream) {
        this.stream = stream;
        return this;
    }

    public StreamObserver<?> getStreamObserver() {
        return streamObserver;
    }

    public Request setStreamObserver(StreamObserver<?> streamObserver) {
        this.streamObserver = streamObserver;
        return this;
    }

    @Override
    public String toString() {
        return "Request{" +
                "clazz=" + clazz +
                ", method='" + method + '\'' +
                ", stream=" + stream +
                ", requestId=" + requestId +
                ", data=" + data +
                '}';
    }
}
