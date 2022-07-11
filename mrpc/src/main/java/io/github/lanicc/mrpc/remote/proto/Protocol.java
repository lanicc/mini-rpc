package io.github.lanicc.mrpc.remote.proto;

/**
 * Created on 2022/7/9.
 *
 * @author lan
 */
public class Protocol {

    protected long requestId;

    protected Object data;

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
