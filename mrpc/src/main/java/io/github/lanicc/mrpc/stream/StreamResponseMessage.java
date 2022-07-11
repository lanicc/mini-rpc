package io.github.lanicc.mrpc.stream;

import io.github.lanicc.mrpc.remote.proto.Response;

/**
 * Created on 2022/7/11.
 *
 * @author lan
 */
public class StreamResponseMessage<T> extends Response {


    private boolean complete;

    public StreamResponseMessage() {
    }

    public StreamResponseMessage(long requestId, T data, boolean complete) {
        this.data = data;
        this.complete = complete;
        setRequestId(requestId);
    }

    public boolean isComplete() {
        return complete;
    }

    public StreamResponseMessage<T> setComplete(boolean complete) {
        this.complete = complete;
        return this;
    }
}
